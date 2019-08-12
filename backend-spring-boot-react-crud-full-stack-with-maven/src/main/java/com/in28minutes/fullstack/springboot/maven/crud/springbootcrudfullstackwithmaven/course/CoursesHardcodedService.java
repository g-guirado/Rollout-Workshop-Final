package com.in28minutes.fullstack.springboot.maven.crud.springbootcrudfullstackwithmaven.course;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;
import com.in28minutes.fullstack.springboot.maven.crud.springbootcrudfullstackwithmaven.FlagsContainer;

import io.rollout.configuration.RoxContainer;
import io.rollout.rox.server.Rox;

@Service
public class CoursesHardcodedService {

	private static List<Course> courses = new ArrayList<>();
	private static long idCounter = 0;
	private static FlagsContainer conf = new FlagsContainer();

	static {
		courses.add(new Course(++idCounter, "in28minutes", "Learn Full stack with Spring Boot and Angular"));
		courses.add(new Course(++idCounter, "in28minutes", "Learn Full stack with Spring Boot and React"));
		courses.add(new Course(++idCounter, "in28minutes", "Master Microservices with Spring Boot and Spring Cloud"));
		courses.add(new Course(++idCounter, "in28minutes",
				"Deploy Spring Boot Microservices to Cloud with Docker and Kubernetes"));
		try {
			Rox.register("default", conf);
			Rox.setup("5d41ce5f2b1633417b3408a4").get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Course> findAll() {
		return courses;
	}

	public Course save(Course course) {
		if(conf.newSave.isEnabled()) {
			System.out.println("Save flag is enabled");
			List<Course> tempCourses = new ArrayList<>();
			if (course.getId() == -1 || course.getId() == 0) {
				for(int i=0; i<courses.size(); i++){
					Long currId = courses.get(i).getId();
					Long newId = currId+1;
					courses.get(i).setId(newId);
					tempCourses.add(courses.get(i));
				}
				course.setId((long) 1);
				courses.add(0, course);
			} else {
				deleteById(course.getId());
				courses.add(course);
			}
		}
		else{
			System.out.println("Save flag not enabled");
			if (course.getId() == -1 || course.getId() == 0) {
				course.setId(++idCounter);
				courses.add(course);
			} else {
				deleteById(course.getId());
				courses.add(course);
			}
		}
		return course;
	}

	public Course deleteById(long id) {
		FlagsContainer conf = new FlagsContainer();
		if(conf.multiFlag.isEnabled()) {
			Course course = findById(id);

			if (course == null)
				return null;

			if (courses.remove(course)) {
				return course;
			}
		}

		return null;
	}

	public Course findById(long id) {
		for (Course course : courses) {
			if (course.getId() == id) {
				return course;
			}
		}

		return null;
	}
}
