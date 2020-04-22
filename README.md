# repo-himalaya
这是一个仓库-喜马拉雅仿

连接测试

this is a test

yu test



<<<<<<< HEAD
进度P7 
=======
已完成进度P4 LogUtil
>>>>>>> refs/remotes/origin/master



配置build.gradle 中阿里镜像

```java
// 添加阿里云 maven 地址
maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
maven { url 'http://maven.aliyun.com/nexus/content/repositories/jcenter' }
```

配置build.gradle:app 中依赖

```
implementation files('libs/TingPhoneOpenSDK_7.1.6.jar')
implementation files('libs/XmDownload_1.7.jar')
implementation files('libs/XmlyOAuth2SDK_V1.11.jar')
implementation 'com.google.code.gson:gson:2.8.1'
implementation 'com.squareup.okhttp3:okhttp:3.11.0'
implementation 'com.squareup.okio:okio:1.15.0'
implementation 'androidx.legacy:legacy-support-v4:1.0.0'
```



##### 喜马拉雅集成sdk

从喜马拉雅sdk平台下载最新版sdk、参考开发文档

初始化接口-设置app_secret(密钥) 

并获取每个类别categroies

```java
Map<String, String> map = new HashMap<>();
CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {   
    @Override    
    public void onSuccess(@Nullable CategoryList categoryList) {        
        List<Category> categories = categoryList.getCategories();        
        if (categories != null) {            
            int size = categories.size();            
            Log.d(TAG, "categories size ---- <<" + size);            
            for (Category category : categories) {               
                Log.d(TAG, "category ------>" + category.getCategoryName());            
            }        
        }   
    }   
    @Override    
    public void onError(int code, String message) {        
        Log.d(TAG , "error code --" + code + "error message" + message);    
    }});
```

<<<<<<< HEAD
`
遇到的问题;安卓SDK升级后，V4包会找不到报错,更新后android.support.v4.view.ViewPager被androidx.viewpager.widget.ViewPager所取代，所有关于v4的包都要替换掉。

具体如何修改：修改XML文件中的android.support.v4.view.ViewPager为androidx.viewpager.widget.ViewPager。删除引入的所有关于v4的包，设置Android Studio自动导包，自动补全包即可。
=======
因为android版本27以上、拒绝http明文请求。所以会callback 回调连接失败

解决方案：在res目录下新建xml/network_security_config.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>    
    <base-config cleartextTrafficPermitted="true" />
</network-security-config>
```
>>>>>>> refs/remotes/origin/master

并在Manifests声明 android:roundIcon="@mipmap/ic_launcher_round"

增加Logutil工具类、方便管理打印日志、在BaseApplication.java中添加

```
//初始化 false 打印log ; true 取消打印
logLogUtil.init(this.getPackageName(),false);
```



