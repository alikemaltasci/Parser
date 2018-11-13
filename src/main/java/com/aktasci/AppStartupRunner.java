package com.aktasci;

import com.aktasci.process.AccessLogProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppStartupRunner implements ApplicationRunner {

    @Autowired
    private AccessLogProcessor accessLogProcessor;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        accessLogProcessor.process(args);
    }
}
