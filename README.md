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
HttpBird.download("http://www.down.com/xxx.apk", "xxx.apk", new FileLoadingListener() {
            @Override
            public void onLoadingStarted(String url) {
                mTvDown.setText("正在下载");
            }

            @Override
            public void onProgressUpdate(String url, long current, long total) {
                mPbProgress.setMax((int) total);
                mPbProgress.setProgress((int) current);
            }

            @Override
            public void onLoadingComplete(String url, File loadedFile, String response) {
                mTvDown.setText("下载完成");
            }

            @Override
            public void onLoadingFailed(String url, String message) {
                mTvDown.setText("下载失败");
            }

            @Override
            public void onLoadingCancelled(String url) {
                mTvDown.setText("下载暂停");
            }
        });
```

### 上传
```java

HashMap paramMap = new HashMap();
HashMap fileMap = new HashMap();
HttpBird.upload("", paramMap, fileMap, new FileLoadingListener() {
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
        });
```


