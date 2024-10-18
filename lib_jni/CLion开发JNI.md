# CLion开发JNI.md
https://blog.csdn.net/qq_33163046/article/details/129695472

## 1. 生成头文件
jdk10以后通过javac生成
javac -h jni_output_folder .\TestJni.java

## 2. CLion创建C项目 library类型 shared动态库

## 3. 复制jni头文件到项目中

## 4. 配置jni库
- CMakeList.txt中添加
```CMake
#添加jni头文件环境变量
find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})
```

## 5. c实现jni头文件中的JNI方法

## 6. 配置交叉编译环境
- ndk添加到环境变量Path中
- 查看是否配置成功 ndk-build.cmd -v 命令
- Toolchains配置 
	- 操作入口: File--Settings--Build,Execution,Deployment--Toolchains--MinGW(default)
	- 修改BuildTool: `C:\Android\Sdk\ndk\26.1.10909125\prebuilt\windows-x86_64\bin\make.exe`
	- 修改C Complier: `C:\Android\Sdk\ndk\26.1.10909125\toolchains\llvm\prebuilt\windows-x86_64\bin\clang.exe`
	- 修改C++ Complier: `C:\Android\Sdk\ndk\26.1.10909125\toolchains\llvm\prebuilt\windows-x86_64\bin\clang++.exe`
- CMake配置
	- 操作入口: File--Settings--Build,Execution,Deployment--CMake--Debug-MinGW
	- 修改ToolChain为MinGW
	- 修改CMake options 点击展开进入编辑,输入以下内容
	```javascript
	-DCMAKE_TOOLCHAIN_FILE="C:\Android\Sdk\ndk\26.1.10909125\build\cmake\android.toolchain.cmake"
	-DCMAKE_SYSTEM_NAME=Android
	-DANDROID_ABI=arm64-v8a
	-DCMAKE_ANDROID_NDK="C:\Android\Sdk\ndk\26.1.10909125"
	-DCMAKE_SYSTEM_VERSION=21
	-DCMAKE_C_FLAGS=""
	-DCMAKE_CXX_FLAGS=""
	-DCMAKE_ANDROID_NDK_TOOLCHAIN_VERSION=clang
	```
	- 这里同时设置
	-DANDROID_ABI=arm64-v8a,armeabi-v7a会报错,原因未知,暂时先设置一个
## 7. 编译
- Build-Rebuild Project
- 产物目录: cmake-build-debug-mingw文件夹下

## 8. 同时编译v8a,v7a,x86,x86_64
- CMake配置
	- 操作入口: File--Settings--Build,Execution,Deployment--CMake 
	- 创建多个编译配置 Debug-MinGW-v8a Debug-MinGW-v7a Debug-MinGW-x86_64 Debug-MinGW-x86
	- 不同的编译设置 在Cmake options中修改DANDROID_ABI即可
	```javascript
	...
	-DANDROID_ABI=xxx
	...
	```




ndk {
       abiFilters 'armeabi-v7a', 'arm64-v8a', 'x86', 'x86_64'
   }