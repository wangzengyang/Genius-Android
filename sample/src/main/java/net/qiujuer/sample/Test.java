package net.qiujuer.sample;

import android.content.Context;
import android.widget.Toast;

import net.qiujuer.genius.command.CommandModel;
import net.qiujuer.genius.journal.LogCallbackListener;
import net.qiujuer.genius.journal.LogData;
import net.qiujuer.genius.journal.LogUtils;
import net.qiujuer.genius.utils.GlobalValue;

/**
 * Created by Genius on 2014/8/13.
 * 测试单元
 */
public class Test {
    public Test(Context context) {
        //初始化全局Context
        GlobalValue.setContext(context);
    }

    /**
     * 测试命令行执行
     */
    public void testCommand() {
        Thread thread = new Thread() {
            public void run() {
                //执行命令，后台服务自动控制
                //调用方式与ProcessBuilder传参方式一样
                CommandModel processModel = new CommandModel("/system/bin/ping",
                        "-c", "4", "-s", "100",
                        "www.baidu.com");
                String res = CommandModel.command(processModel);

                LogUtils.i(Test.class.getName(), "Ping 测试结果：" + res);
            }
        };
        thread.start();
    }

    /**
     * 日志测试
     */
    public void testLogs() {
        //是否调用Android Log类
        LogUtils.setCallLog(true);

        //是否开启写入文件
        LogUtils.setSaveLog(GlobalValue.getContext(), true, 10, 1, null);

        //设置是否监听外部存储插入操作
        //开启时插入外部设备（SD）时将拷贝存储的日志文件到外部存储设备
        //此操作依赖于是否开启写入文件功能，未开启则此方法无效
        LogUtils.setCopyExternalStorage(GlobalValue.getContext(), true, "Test/Logs");

        //添加回调
        LogUtils.addLogCallbackListener(new LogCallbackListener() {
            @Override
            public void OnLogArrived(LogData data) {
                //有日志写来了
                Toast.makeText(GlobalValue.getContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnListenerAdded(LogData[] dataArray) {
                //添加监听后自动读取缓存
                if (dataArray != null)
                    for (LogData data : dataArray) {
                        OnLogArrived(data);
                    }
            }
        });

        //设置日志等级
        //VERBOSE为5到ERROR为1依次递减
        LogUtils.setLevel(LogUtils.ALL);


        LogUtils.v(Test.class.getName(), "测试日志 VERBOSE 级别。");
        LogUtils.d(Test.class.getName(), "测试日志 DEBUG 级别。");
        LogUtils.i(Test.class.getName(), "测试日志 INFO 级别。");
        LogUtils.w(Test.class.getName(), "测试日志 WARN 级别。");
        LogUtils.e(Test.class.getName(), "测试日志 ERROR 级别。");

        LogUtils.setLevel(LogUtils.WARN);
        LogUtils.v(Test.class.getName(), "二次测试日志 VERBOSE 级别。");
        LogUtils.d(Test.class.getName(), "二次测试日志 DEBUG 级别。");
        LogUtils.i(Test.class.getName(), "二次测试日志 INFO 级别。");
        LogUtils.w(Test.class.getName(), "二次测试日志 WARN 级别。");
        LogUtils.e(Test.class.getName(), "二次测试日志 ERROR 级别。");
    }
}