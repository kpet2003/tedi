package com.tediproject.tedi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tediproject.tedi.dto.ApplicantDto;
import com.tediproject.tedi.dto.JobDto;
import com.tediproject.tedi.dto.NewJobDto;
import com.tediproject.tedi.security.JwtUtil;
import com.tediproject.tedi.service.JobService;

@RestController
public class JobController {
    
    @Autowired
    private JobService jobService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value="/newJob")
    public ResponseEntity<?> createJob(@RequestBody NewJobDto job){
        try {
            jwtUtil.validateToken(job.getAuthorToken());
            jobService.createJob(job);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/connectionjobs/{user}")
    public ResponseEntity<?> getConnectionJobs(@PathVariable String user){
        try {
            jwtUtil.validateToken(user);
            List<JobDto> allJobs = jobService.getJobs(user);
            List<JobDto> jobs = jobService.getConnectionsJobs(allJobs,user);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/otherjobs/{user}")
    public ResponseEntity<?> getOtherJobs(@PathVariable String user){
        try {
            jwtUtil.validateToken(user);
            List<JobDto> allJobs = jobService.getJobs(user);
            List<JobDto> jobs = jobService.getOtherJobs(allJobs,user);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/myjobs/{user}")
    public ResponseEntity<?> getMyJobs(@PathVariable String user){
        try {
            jwtUtil.validateToken(user);
            List<JobDto> allJobs = jobService.getJobs(user);
            List<JobDto> jobs = jobService.getMyJobs(allJobs,user);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    @PostMapping(value = "/jobs/{jobId}/{token}")
    public ResponseEntity<?> apply(@PathVariable long jobId, @PathVariable String token){
        try {
            jwtUtil.validateToken(token);
            jobService.applyForJob(jobId, token);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value="/applied/{token}")
    public ResponseEntity<?> getAppliedJobs(@PathVariable String token){
        try {
            jwtUtil.validateToken(token);
            List<JobDto> allJobs = jobService.getJobs(token);
            List<JobDto> jobs = jobService.getAppliedJobs(allJobs,token);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value="/applicants/{jobId}/{token}")
    public ResponseEntity<?> getApplicants(@PathVariable long jobId, @PathVariable String token){
        try {
            jwtUtil.validateToken(token);
            List<ApplicantDto> applicants = jobService.getApplicants(jobId);
            return ResponseEntity.ok(applicants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value="/addView/{jobId}/{token}")
    public ResponseEntity<?> addView(@PathVariable long jobId, @PathVariable String token) {
        try {
            jwtUtil.validateToken(token);
            
            jobService.addView(jobId, token);

            return ResponseEntity.ok(HttpStatus.OK);
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
