
1. 需要自己创建 BaseResponseBodyAdvice 并注册为bean 对象（非必须）
    主要是自动包装的返回值，所需要支持的controller 包路径
2. 需要自己创建枚举 ErrorCodeEnums(类名自定义) 并实现接口 IErrorCode
    同时需要创建文件 META-INF/services/com.suyh.base.web.error.IErrorCode 并将该枚举类的完全限定类名写在里面
3. 需要实现一个 AbstractAuthenticationInterceptor 的派生类，并注册的 bean 对象
4. 需要定义一个 LoginUser 并派生成 AbstractLoginUser


