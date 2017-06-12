# 注册机使用方法
## 生成密钥和公钥
1. 运行 Main 方法，会在控制台输出 Key 和 Result，记录下 Key 和 Result 的值
2. 安装官方版 Mybatis Plugin 插件,然后关闭IDEA (安装插件方法自行谷歌)
3. hosts 中添加 127.0.0.1 www.codesmagic.com (修改方法自行谷歌)
4. 记事本打开 `C:\Users\{USER}\.IntelliJIdea{VERSION}\config\options\mybatis.xml`，将 Key 和 Result 的值写入到对应的字段中
5. 打开 IDEA，此时 Mybatis Plugin 插件已经激活

## Mac 下打开 mybatis.xml 配置文件的方法：
终端输入下面的命令将会用 vim 编辑器打开配置文件

```
cd ~/Library/Preferences/IntelliJIdea{版本号}/options/
open mybatis.xml
```
>若想使用其他编辑器打开可以使用 open 命令的 -a 参数，例如使用 Sublime Text 打开：
>
```
open mybatis.xml -a Sublime\ Text
```
>空格需要转义