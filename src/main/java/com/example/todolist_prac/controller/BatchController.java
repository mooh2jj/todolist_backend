package com.example.todolist_prac.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {

    private final ApplicationContext context;
    private final JobLauncher jobLauncher;
    private final Job todoNotificationJob;

    @GetMapping("/job")
    public String startJob() throws Exception {
        System.out.println("Starting the batch job");
        System.out.println("job: " +todoNotificationJob);

        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("timestamp", new JobParameter(System.currentTimeMillis()));
        JobExecution jobExecution = jobLauncher.run(todoNotificationJob, new JobParameters(parameters));
        return "Batch job "+ jobExecution.getStatus();

    }

    // 아직 안됨!
/*    @PostMapping("/run")
    public ExitStatus runJob(@RequestBody JobLauncherRequest request) throws Exception {
        Job job = this.context.getBean(request.getName(), Job.class);
        System.out.println("job:" + job);

        return this.jobLauncher.run(job, request.getJobParameters())
                .getExitStatus();
    }*/
}
