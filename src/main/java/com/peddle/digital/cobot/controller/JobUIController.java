package com.peddle.digital.cobot.controller;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.peddle.digital.cobot.Util.JobUtil;
import com.peddle.digital.cobot.model.Job;
import com.peddle.digital.cobot.repository.JobRepository;



@Controller
@RequestMapping("/ui/job/")
public class JobUIController {
	final static Logger logger = Logger.getLogger(JobUIController.class);

	@Autowired
    JobRepository jobRepository;
	
	@GetMapping("all")
    public String showAllJobs(Model model) {
		logger.info("get all jobs");
		List<Job> jobs=jobRepository.findAll();
		model.addAttribute("jobs", jobs);
        return "index";
    }
	
}
