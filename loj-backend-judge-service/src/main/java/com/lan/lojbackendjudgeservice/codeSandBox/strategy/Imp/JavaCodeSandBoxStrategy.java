package com.lan.lojbackendjudgeservice.codeSandBox.strategy.Imp;


import com.lan.lojbackendcommonservice.dto.enums.ExecuteStatusEnum;
import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteContext;
import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteResponse;
import com.lan.lojbackendjudgeservice.codeSandBox.model.Metrics;
import com.lan.lojbackendjudgeservice.codeSandBox.strategy.CodeSandBoxStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class JavaCodeSandBoxStrategy implements CodeSandBoxStrategy {

    private final  String fileName = "Main.java";
    private final String path ="src/main/resources/code/";
    public static final List<String> blackList = new ArrayList<>();
    static {
        blackList.add("java.io");
        blackList.add("Process");
        blackList.add("Runtime");
    }


    @Override
    public ExecuteResponse Execute(ExecuteContext executeContext) {

        // 1）将代码写入并保存到文件里。
        String testCode = executeContext.getTestCode();
        String code = executeContext.getCode();

        //判断是否有黑名单中的危险操作
        for (String black : blackList) {
           if (code.contains(black)){
               throw new RuntimeException("危险操作,无权限");
           }
        }

        //合并测试代码和提交的代码
        String fullCode = testCode+code;

        //创建目录
        String codePath = path + UUID.randomUUID()+ File.separator;
        File file = new File(codePath);
        if (!file.exists()){
            file.mkdirs();
        }
        //创建文件

        ExecuteResponse executeResponse = new ExecuteResponse();

        file = new File(codePath+fileName);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error("文件创建失败 "+e.getMessage());
                executeResponse.setExecuteInfo("文件创建失败 "+e.getMessage());
                executeResponse.setExecuteStatus(ExecuteStatusEnum.FAILED.getValue());
                return executeResponse;

            }
        }
        //写入数据到文件里
        try {
            FileUtils.writeByteArrayToFile(file,fullCode.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("写入文件失败 "+e.getMessage());
            executeResponse.setExecuteInfo("写入文件失败 "+e.getMessage());
            executeResponse.setExecuteStatus(ExecuteStatusEnum.FAILED.getValue());
            return executeResponse;
        }
        System.out.println("写入完成");

        // 2）在程序里编译文件为class文件。
        Process process;
        try {
            process = Runtime.getRuntime().exec(String.format("javac -encoding UTF-8 %s", file.getPath()));
        } catch (IOException e) {
            log.error("编译执行错误 "+e.getMessage());
            executeResponse.setExecuteInfo("编译执行错误 "+e.getMessage());
            executeResponse.setExecuteStatus(ExecuteStatusEnum.FAILED.getValue());
            return executeResponse;
        }


        //获取输出流
        InputStream inputStream = process.getInputStream();
        List<String> readLines = null;
        try {
            readLines = IOUtils.readLines(inputStream, String.valueOf(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String output = StringUtils.join(readLines, "\n");
        if (StringUtils.isNotBlank(output)){
            log.info("编译输出信息:{}",output);
        }

        //获取错误流
        InputStream errorStream = process.getErrorStream();

        try {
            readLines = IOUtils.readLines(errorStream, String.valueOf(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String errorOutput = StringUtils.join(readLines, "\n");
        if (StringUtils.isNotBlank(errorOutput)){
            log.info("编译错误输出信息:{}",errorOutput);
            executeResponse.setExecuteInfo("编译错误输出信息:"+errorOutput);
            executeResponse.setExecuteStatus(ExecuteStatusEnum.FAILED.getValue());
            return executeResponse;

        }
        //等待执行完成 正常输出状态码0
        int  waitFor=0;
        try {
            waitFor = process.waitFor();
            if (waitFor!=0){
                log.error("编译错误，错误码："+waitFor);
                executeResponse.setExecuteInfo("编译错误，错误码："+waitFor);
                executeResponse.setExecuteStatus(ExecuteStatusEnum.FAILED.getValue());
                return executeResponse;
            }
        } catch (Exception e) {
            log.error("编译错误 "+e.getMessage());
            executeResponse.setExecuteInfo("编译错误 "+e.getMessage());
            executeResponse.setExecuteStatus(ExecuteStatusEnum.FAILED.getValue());
            return executeResponse;

        }
        // 3）在程序中执行class文件，得到执行结果。
        //执行class代码

        //统计执行时间
        StopWatch stopWatch=new StopWatch();
        Runtime runtime = Runtime.getRuntime();
        AtomicBoolean isDone = new AtomicBoolean(false);
        try {



            stopWatch.start();
            //设置JVM最大堆内存1024M
            process = runtime.exec(String.format("java -Xmx1024m -Dfile.encoding=UTF-8 -cp %s %s",codePath, "Main"));

        } catch (IOException e) {
            log.error("执行错误 "+e.getMessage());
            executeResponse.setExecuteInfo("执行错误 "+e.getMessage());
            executeResponse.setExecuteStatus(ExecuteStatusEnum.FAILED.getValue());
            return executeResponse;
        }
        //开启守护线程，监听程序执行时间，超过时间自动kill
        Process finalProcess = process;
        new Thread(()->{
            try {
                Thread.sleep(10000);
                if (!isDone.get()){
                    log.info("执行时间超过10s，自动kill");
                    finalProcess.destroyForcibly();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        //等待执行完成 正常输出状态码0
        waitFor=0;
        try {
            waitFor = process.waitFor();
            if (waitFor!=0){
                log.error("执行错误，错误码："+waitFor);
            }
            stopWatch.stop();
            isDone.set(true);
        } catch (Exception e) {
            log.error("编译错误 "+e.getMessage());
            executeResponse.setExecuteInfo("编译错误 "+e.getMessage());
            executeResponse.setExecuteStatus(ExecuteStatusEnum.FAILED.getValue());
            return executeResponse;

        }

        //获取输出流
        inputStream = process.getInputStream();
        try {
            readLines = IOUtils.readLines(inputStream, String.valueOf(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = null;
        output = StringUtils.join(readLines, "\n");
        if (StringUtils.isNotBlank(output)){
            log.info("执行输出信息:{}",output);
            result=output;
        }
        //获取错误流
        errorStream = process.getErrorStream();
        try {
            readLines = IOUtils.readLines(errorStream, String.valueOf(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        errorOutput = StringUtils.join(readLines, "\n");
        if (StringUtils.isNotBlank(errorOutput)){
            log.error("执行错误输出信息:{}",errorOutput);
            executeResponse.setExecuteInfo("执行错误输出信息:"+errorOutput);
            executeResponse.setExecuteStatus(ExecuteStatusEnum.FAILED.getValue());
            return executeResponse;
        }

        // 5）搜集和整理输出结果。
        List<String> outputList =new ArrayList<>();
        if (StringUtils.isNotBlank(result)){
            String[] resultArray = result.split("&&");
            for (String item : resultArray) {
                outputList.add(item);
            }
        }
        // 6）获取执行文件的性能指标，比如：执行时间、占用内存等等。
        long timeMillis = stopWatch.getTotalTimeMillis();
        log.info("执行时间：{}",timeMillis);
        long memory = runtime.totalMemory() - runtime.freeMemory();
        log.info("占用内存：{}",memory);
        // 7) 文件清理与释放。
        try {
            log.info("删除文件："+codePath);
            FileUtils.deleteDirectory(new File(codePath));
        } catch (IOException e) {
            log.error("文件清理错误 "+e.getMessage());
            e.printStackTrace();
        }
        process.destroy();

        executeResponse.setOutputList(outputList);
        executeResponse.setExecuteInfo(ExecuteStatusEnum.FINISH.getText());
        executeResponse.setExecuteStatus(ExecuteStatusEnum.FINISH.getValue());
        executeResponse.setMetrics(Metrics.builder().time(timeMillis).memory(memory).build());

        return executeResponse;
    }


}

