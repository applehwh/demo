package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.util.stream.Stream;

@RestController
public class demoContorller {
    @GetMapping("/demo")
    public Mono<String> demo1(String cmd) throws Exception {
        return Mono.just(execCmd(cmd).findFirst().get());
    }

    @GetMapping("/demo2")
    public Flux<?> demo2(String cmd) throws Exception {
        return Flux.fromStream( execCmd(cmd));
    }

    /**
     * 执行系统命令, 返回执行结果
     *
     * @param cmd 需要执行的命令
     */
    private Stream<String> execCmd(String cmd) throws Exception {
        Stream<String> lines;

        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        try {
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(cmd);

            // 方法阻塞, 等待命令执行完成（成功会返回0）
       //     process.waitFor();

            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "gbk"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));
            lines= bufrError.lines();
       //     lines.forEach(System.out::println);

//            System.out.println(bufrIn.readLine());
//            System.out.println(bufrError.readLine());

        } finally {
          //  closeStream(bufrIn);
          //  closeStream(bufrError);

            // 销毁子进程
//            if (process != null) {
//                process.destroy();
//            }
        }

        // 返回执行结果
        return lines;
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }
}
