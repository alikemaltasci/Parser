package com.ef.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.ef.data.model.AccessLog;
import com.ef.data.repository.AccessLogRepository;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccessLogReportJobConfigTest {

    private static final String PAR_FILE_PATH = "filePath";
    private static final String PAR_UNIQUE = "uniqueness";
    private static final String PAR_VAL_FILE_PATH = "src/test/resources/access-test.log";
    private static final String CHUNK_STEP = "CHUNK_STEP";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private AccessLogReportJobConfig config;

    @Autowired
    private AccessLogRepository accessLogRepository;

    private JobParameters jobParameters;

    @Before
    public void setUp() {
        jobParameters = new JobParametersBuilder().addString(PAR_FILE_PATH, PAR_VAL_FILE_PATH)
                .addLong(PAR_UNIQUE, System.nanoTime()).toJobParameters();
    }

    @Test
    public void entireJob() throws Exception {
        final JobExecution result = jobLauncherTestUtils.getJobLauncher()
                .run(config.accessLogReportJob(), jobParameters);
        assertNotNull(result);
        assertEquals(BatchStatus.COMPLETED, result.getStatus());
        assertEquals(5, ((List<AccessLog>) accessLogRepository.findAll()).size());
    }

    @Test
    public void chunkStep() {
        assertEquals(BatchStatus.COMPLETED, jobLauncherTestUtils.launchStep(CHUNK_STEP, jobParameters).getStatus());
        assertEquals(5, ((List<AccessLog>) accessLogRepository.findAll()).size());
    }
}
