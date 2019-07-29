# HttpBird
android  上传下载框架,简单使用~

## 使用
使用方法,在使用的子 build.gradle 中添加配置
```
implementation 'vip.ruoyun.http:httpbird:1.0.0'
```

### 初始化

初始化参数
```java
HttpBirdConfiguration configuration = new HttpBirdConfiguration.Builder(this)
                .builderIsDebug(true)
                .builderIsChunked(true)
                .builderCachePath("")
                .build();
HttpBird.init(configuration);
```

### 下载

```java
HttpBird.download("")
        .targetName("")
        .listener(new FileLoadingListener() {
            @Override
            public void onLoadingStarted(String url) {

            }

            @Override
            public void onProgressUpdate(String url, long current, long total) {

            }

            @Override
            public void onLoadingComplete(String url, File loadedFile, String response) {

            }

            @Override
            public void onLoadingFailed(String url, String message) {

            }

            @Override
            public void onLoadingCancelled(String url) {

            }
        }).go();
```

### 上传
```java
HttpBird.upload("")
        .file("", new File(""))
        .param("", "")
        .header("", "")
        .listener(new FileLoadingListener() {
            @Override
            public void onLoadingStarted(String url) {

            }

            @Override
            public void onProgressUpdate(String url, long current, long total) {

            }

            @Override
            public void onLoadingComplete(String url, File loadedFile, String response) {

            }

            @Override
            public void onLoadingFailed(String url, String message) {

            }

            @Override
            public void onLoadingCancelled(String url) {

            }
        }).go();
```


