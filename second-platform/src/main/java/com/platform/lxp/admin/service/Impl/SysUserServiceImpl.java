package com.platform.lxp.admin.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.lxp.admin.entity.dto.SysUserDTO;
import com.platform.lxp.admin.entity.dto.WxLoginDTO;
import com.platform.lxp.admin.entity.pojo.SysUser;
import com.platform.lxp.admin.entity.pojo.User;
import com.platform.lxp.admin.entity.vo.UserLoginVO;
import com.platform.lxp.admin.local.UserContext;
import com.platform.lxp.admin.mapper.SysUserMapper;
import com.platform.lxp.admin.mapper.UserMapper;
import com.platform.lxp.admin.service.ISysUserService;
import com.platform.lxp.admin.utils.HttpClientUtil;
import com.platform.lxp.admin.utils.PasswordUtil;
import com.platform.lxp.admin.utils.TokenUtil;
import com.platform.lxp.common.Constants.MessageConstant;
import com.platform.lxp.common.ResponseResult;
import com.platform.lxp.common.exception.BusinessException;
import com.platform.lxp.common.properties.WechatProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.platform.lxp.common.Constants.MessageConstant.OCR_NOT_FOUND_NAME_STUDENTID;

/**
 * @author 来晓璞
 * @date 2026/3/30 18:27
 * @Description:
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static final String WX_USERINFO = "https://api.weixin.qq.com/sns/userinfo";

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private WechatProperties weChatProperties;

    @Resource
    private UserMapper userMapper;

    @Override
    public List<SysUser> selectList(SysUserDTO dto) {
        LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<SysUser>();
        handlerQuery(query, dto);
        List<SysUser> sysUsers = sysUserMapper.selectList(query);
        return sysUsers;
    }

    /**
     * 拍照登录/注册
     * @param name 姓名
     * @param studentId 校园卡ID/学号
     * @return token
     */
    @Transactional
    public SysUser login(String name, String studentId) {
        if (name == null || studentId == null) {
            throw new BusinessException(OCR_NOT_FOUND_NAME_STUDENTID);
        }

        // 查询是否存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getName, name)
                .eq(SysUser::getStudentId, studentId);

        SysUser sysUser = sysUserMapper.selectOne(wrapper);

        if (sysUser != null) {
            // 用户存在，执行登录
            return doLogin(sysUser);
        } else {
            // 用户不存在，执行注册
            return doRegister(name, studentId);
        }
    }


    /**
     * 执行登录
     */
    private SysUser doLogin(SysUser sysUser) {
        // 生成新token
        String newToken = TokenUtil.generateToken();
        LocalDateTime now = LocalDateTime.now();
        Date dateNow = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        // 计算过期时间（假设 TOKEN_EXPIRE_HOURS 在某个地方定义）
        LocalDateTime expireTime = now.plusHours(24); // 假设过期24小时，替换为你的实际常量

        // 更新token和过期时间
        sysUser.setToken(newToken);
        sysUser.setTokenExpireTime(expireTime);
        sysUser.setUpdateTime(dateNow);
        sysUserMapper.updateById(sysUser);

        // 保存到ThreadLocal
        UserContext.setCurrentUser(sysUser);

        return sysUser;
    }

    /**
     * 执行注册
     */
    private SysUser doRegister(String name, String studentId) {
        SysUser newUser = new SysUser();
        newUser.setName(name);
        newUser.setStudentId(studentId);
        // 默认密码为校园卡ID，并进行加密存储
        newUser.setPassword(PasswordUtil.encode(studentId));

        // 生成token
        String token = TokenUtil.generateToken();
        LocalDateTime expireTime = LocalDateTime.now().plusHours(SysUser.TOKEN_EXPIRE_HOURS);
        newUser.setToken(token);
        newUser.setTokenExpireTime(expireTime);

        newUser.setCreateTime(new Date());
        newUser.setUpdateTime(new Date());

        sysUserMapper.insert(newUser);

        // 保存到ThreadLocal
        UserContext.setCurrentUser(newUser);

        return newUser;
    }

    /**
     * 验证token并获取用户信息
     */
    public SysUser validateToken(String token) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getToken, token);

        SysUser user = sysUserMapper.selectOne(wrapper);

        if (user == null) {
            throw new BusinessException("token无效");
        }

        if (TokenUtil.isTokenExpired(user.getTokenExpireTime())) {
            throw new BusinessException("token已过期");
        }

        return user;
    }

    /**
     * 退出登录
     */
    public void logout() {
        SysUser currentUser = UserContext.getCurrentUser();
        if (currentUser != null && currentUser.getId() != null) {
            // 清除token
            currentUser.setToken(null);
            currentUser.setTokenExpireTime(null);
            sysUserMapper.updateById(currentUser);
        }
        UserContext.clear();
    }

    /**
     * 修改密码
     */
    public void changePassword(String oldPassword, String newPassword) {
        SysUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new BusinessException("用户未登录");
        }

        // 验证旧密码
        if (!PasswordUtil.matches(oldPassword, currentUser.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        // 更新密码
        currentUser.setPassword(PasswordUtil.encode(newPassword));
        sysUserMapper.updateById(currentUser);
    }

    @Override
    public void update(SysUserDTO dto) {
        if (ObjectUtil.isEmpty(dto.getId())){
            throw new BusinessException(MessageConstant.ID_IS_EMPTY);
        }
        SysUser sysUser = sysUserMapper.selectById(dto.getId());
        if (ObjectUtil.isEmpty(sysUser)) {
            throw new BusinessException(MessageConstant.ACCOUNT_NOT_EXIST);
        }
        SysUser user = new SysUser();
        BeanUtil.copyProperties(dto, user);
        sysUserMapper.updateById(user);
    }

    @Override
    public ResponseResult wxLogin(WxLoginDTO dto) {

        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppId());
        map.put("secret", weChatProperties.getAppSecret());
        map.put("code", dto.getCode());
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(json);
        // 错误判断
        if (jsonObject.containsKey("errcode")) {
            throw new BusinessException("微信登录失败：" + jsonObject.getString("errmsg"));
        }

        String openid = jsonObject.getString("openid");
        String accessToken = jsonObject.getString("access_token");

        // 调用微信用户信息接口
        Map<String, String> userInfoMap = new HashMap<>();
        userInfoMap.put("access_token", accessToken);
        userInfoMap.put("openid", openid);

        String userInfoJson = HttpClientUtil.doGet(WX_USERINFO, userInfoMap);
        JSONObject userInfo = JSON.parseObject(userInfoJson);
        String nickname = userInfo.getString("nickname");
        String avatar = userInfo.getString("headimgurl");
        Integer sex = userInfo.getInteger("sex");
        String unionid = userInfo.getString("unionid");
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        User user = userMapper.selectOne(wrapper);

        // 4. 未注册则自动注册
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .unionid(unionid)
                    .nickname(nickname)
                    .avatar(avatar)
                    .sex(sex)
                    .build();
            userMapper.insert(user);
        }

        SysUser sysUser = new SysUser();
        String token = TokenUtil.generateToken();
        LocalDateTime expireTime = LocalDateTime.now().plusHours(SysUser.TOKEN_EXPIRE_HOURS);
        sysUser.setToken(token);
        sysUser.setTokenExpireTime(expireTime);

        sysUser.setCreateTime(new Date());
        sysUser.setUpdateTime(new Date());

        sysUserMapper.insert(sysUser);


        // 6. 返回
        UserLoginVO vo = new UserLoginVO();
        vo.setId(user.getId());
        vo.setToken(token);
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        return ResponseResult.success(vo);
    }


    /**
     * 封装通用查询条件
     * @param queryWrapper
     * @param dto
     */
    public void handlerQuery(LambdaQueryWrapper<SysUser> queryWrapper, SysUserDTO dto) {


    }




}
