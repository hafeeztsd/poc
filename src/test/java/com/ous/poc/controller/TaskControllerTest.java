package com.ous.poc.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ous.poc.Application;
import com.ous.poc.model.CreateTaskRequest;
import com.ous.poc.model.TaskResponse;
import com.ous.poc.model.TaskStatus;

/**
 * Integration tests for {@link TaskController}
 * 
 * @author abdulhafeez
 *
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
public class TaskControllerTest {

	private static final String TASK_ENDPOINT = "/task";
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Autowired
	private MockMvc mvc;

	@Test
	public void shouldFailWhenCreateTaskWithoutAllRequiredParameters() throws Exception {
		CreateTaskRequest createTaskRequest = new CreateTaskRequest();
		String jsonPayload = OBJECT_MAPPER.writeValueAsString(createTaskRequest);
		mvc.perform(post(TASK_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonPayload)).andDo(print())
				.andExpect(status().is4xxClientError()).andExpect(jsonPath("$.message", notNullValue()))
				.andExpect(jsonPath("$.subErrors", hasSize(2))).andReturn();
	}

	@Test
	public void shouldFailWhenCreateTaskWithDescripionOnly() throws Exception {
		CreateTaskRequest createTaskRequest = new CreateTaskRequest();
		createTaskRequest.setDescription("Test Description");
		String jsonPayload = OBJECT_MAPPER.writeValueAsString(createTaskRequest);
		mvc.perform(post(TASK_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonPayload)).andDo(print())
				.andExpect(status().is4xxClientError()).andExpect(jsonPath("$.message", notNullValue()))
				.andExpect(jsonPath("$.subErrors", hasSize(1))).andReturn();
	}

	@Test
	public void shouldFailWhenCreateTaskWithTitleOnly() throws Exception {
		CreateTaskRequest createTaskRequest = new CreateTaskRequest();
		createTaskRequest.setTitle("Test Task");
		String jsonPayload = OBJECT_MAPPER.writeValueAsString(createTaskRequest);
		mvc.perform(post(TASK_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonPayload)).andDo(print())
				.andExpect(status().is4xxClientError()).andExpect(jsonPath("$.message", notNullValue()))
				.andExpect(jsonPath("$.subErrors", hasSize(1))).andReturn();
	}

	@Test
	public void shouldCreateTaskSuccessflly() throws Exception {
		CreateTaskRequest createTaskRequest = new CreateTaskRequest();
		createTaskRequest.setTitle("Test Task");
		createTaskRequest.setDescription("Desciption of Test Task.");
		String jsonPayload = OBJECT_MAPPER.writeValueAsString(createTaskRequest);
		mvc.perform(post(TASK_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonPayload)).andDo(print())
				.andExpect(status().is2xxSuccessful()).andReturn();
	}

	@Test
	public void shouldDeleteTaskSuccessflly() throws Exception {
		CreateTaskRequest createTaskRequest = new CreateTaskRequest();
		createTaskRequest.setTitle("Test Task");
		createTaskRequest.setDescription("Desciption of Test Task.");
		String jsonPayload = OBJECT_MAPPER.writeValueAsString(createTaskRequest);

		MvcResult result = mvc.perform(post(TASK_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonPayload))
				.andDo(print()).andExpect(status().is2xxSuccessful()).andReturn();

		String responseJson = result.getResponse().getContentAsString();
		TaskResponse response = OBJECT_MAPPER.readValue(responseJson, TaskResponse.class);

		String uri = TASK_ENDPOINT + "/" + response.getId().toString();
		mvc.perform(delete(uri).contentType(MediaType.APPLICATION_JSON).content(jsonPayload)).andDo(print())
				.andExpect(status().is2xxSuccessful()).andReturn();

	}

	@Test
	public void shouldFindTaskIdBySuccessflly() throws Exception {
		CreateTaskRequest createTaskRequest = new CreateTaskRequest();
		createTaskRequest.setTitle("Test Task");
		createTaskRequest.setDescription("Desciption of Test Task.");
		String jsonPayload = OBJECT_MAPPER.writeValueAsString(createTaskRequest);

		MvcResult result = mvc.perform(post(TASK_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonPayload))
				.andDo(print()).andExpect(status().is2xxSuccessful()).andReturn();

		String responseJson = result.getResponse().getContentAsString();
		TaskResponse response = OBJECT_MAPPER.readValue(responseJson, TaskResponse.class);

		String uri = TASK_ENDPOINT + "/" + response.getId().toString();

		mvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON).content(jsonPayload)).andDo(print())
				.andExpect(status().is2xxSuccessful()).andReturn();

	}

	@Test
	public void shouldFindAllTasksSuccessflly() throws Exception {
		CreateTaskRequest createTaskRequest = new CreateTaskRequest();
		createTaskRequest.setTitle("Test Task");
		createTaskRequest.setDescription("Desciption of Test Task.");
		String jsonPayload = OBJECT_MAPPER.writeValueAsString(createTaskRequest);

		mvc.perform(post(TASK_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonPayload)).andDo(print())
				.andExpect(status().is2xxSuccessful()).andReturn();

		mvc.perform(
				get(TASK_ENDPOINT).contentType(MediaType.APPLICATION_JSON).param("pageNo", "1").param("pageSize", "10"))
				.andDo(print()).andExpect(status().is2xxSuccessful()).andReturn();

	}

	@Test
	public void shouldSuspendTaskSuccessflly() throws Exception {
		CreateTaskRequest createTaskRequest = new CreateTaskRequest();
		createTaskRequest.setTitle("Test Task");
		createTaskRequest.setDescription("Desciption of Test Task.");
		String jsonPayload = OBJECT_MAPPER.writeValueAsString(createTaskRequest);

		MvcResult result = mvc.perform(post(TASK_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonPayload))
				.andDo(print()).andExpect(status().is2xxSuccessful()).andReturn();

		String responseJson = result.getResponse().getContentAsString();
		TaskResponse response = OBJECT_MAPPER.readValue(responseJson, TaskResponse.class);

		String uri = TASK_ENDPOINT + "/" + response.getId().toString() + "/suspend";

		mvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.status", is(TaskStatus.SUSPENDED.getValue())));

	}

}
