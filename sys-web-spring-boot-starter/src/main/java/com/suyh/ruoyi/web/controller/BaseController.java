package com.suyh.ruoyi.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.suyh.base.web.response.dto.R;
import com.suyh.ruoyi.web.domain.page.PageDomain;
import com.suyh.ruoyi.web.domain.page.PageUtils;
import com.suyh.ruoyi.web.domain.page.TableSupport;
import com.suyh.ruoyi.web.domain.AjaxResult;
import com.suyh.ruoyi.web.domain.page.TableDataInfo;
import com.suyh.ruoyi.web.util.RuoyiStringUtils;
import com.suyh.ruoyi.web.util.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * web层通用数据处理
 * 
 * @author ruoyi
 */
@Slf4j
public class BaseController
{
//    /**
//     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
//     */
//    @InitBinder
//    public void initBinder(WebDataBinder binder)
//    {
//        // Date 类型转换
//        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
//        {
//            @Override
//            public void setAsText(String text)
//            {
//                setValue(DateUtils.parseDate(text));
//            }
//        });
//    }

    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageUtils.startPage();
    }

    /**
     * 设置请求排序数据
     */
    protected void startOrderBy()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (RuoyiStringUtils.isNotEmpty(pageDomain.getOrderBy()))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * 清理分页的线程变量
     */
    protected void clearPage()
    {
        PageUtils.clearPage();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.OK.value());
        rspData.setMsg(R.SUCCESS_MSG);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 返回成功
     */
    public AjaxResult success()
    {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error()
    {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message)
    {
        return AjaxResult.success(message);
    }
    
    /**
     * 返回成功消息
     */
    public AjaxResult success(Object data)
    {
        return AjaxResult.success(data);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message)
    {
        return AjaxResult.error(message);
    }

    /**
     * 返回警告消息
     */
    public AjaxResult warn(String message)
    {
        return AjaxResult.warn(message);
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     * 
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result)
    {
        return result ? success() : error();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url)
    {
        return RuoyiStringUtils.format("redirect:{}", url);
    }

//    /**
//     * 获取用户缓存信息
//     */
//    public LoginUser getLoginUser()
//    {
//        return SecurityUtils.getLoginUser();
//    }
//
//    /**
//     * 获取登录用户id
//     */
//    public Long getUserId()
//    {
//        return getLoginUser().getUserId();
//    }
//
//    /**
//     * 获取登录部门id
//     */
//    public Long getDeptId()
//    {
//        return getLoginUser().getDeptId();
//    }
//
//    /**
//     * 获取登录用户名
//     */
//    public String getUsername()
//    {
//        return getLoginUser().getUsername();
//    }
}
