# repo-himalaya
这是一个仓库-喜马拉雅仿

连接测试

this is a test


yu test



进度P3 

配置build.gradle 中阿里镜像

`

```java
// 添加阿里云 maven 地址
maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
maven { url 'http://maven.aliyun.com/nexus/content/repositories/jcenter' }
```

`

配置build.gradle:app 中依赖

`

```
implementation files('libs/TingPhoneOpenSDK_7.1.6.jar')
implementation files('libs/XmDownload_1.7.jar')
implementation files('libs/XmlyOAuth2SDK_V1.11.jar')
implementation 'com.google.code.gson:gson:2.8.1'
implementation 'com.squareup.okhttp3:okhttp:3.11.0'
implementation 'com.squareup.okio:okio:1.15.0'
implementation 'androidx.legacy:legacy-support-v4:1.0.0'
```

`

##### 喜马拉雅集成sdk

从喜马拉雅sdk平台下载最新版sdk、参考开发文档

初始化接口-设置app_secret(密钥) 

并获取每个类别categroies

`

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

`





