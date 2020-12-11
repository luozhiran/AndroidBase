### 网络使用实例

```
       ItgOk.instance()
                .url("http://192.168.40.163:8080/test/multipart1")
                .addHeader("abc", "abc")
                .addMultiFile("file", file)
                .autoCancel(getLifecycle())
                .method(Ok.POST)K
                .ok()
                .go(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        L.e(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        L.e(response.body().string());
                    }
                });
```