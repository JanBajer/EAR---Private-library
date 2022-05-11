package cz.cvut.fel.ear.library.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.cvut.fel.ear.library.environment.Generator;
import cz.cvut.fel.ear.library.exception.NotFoundException;
import cz.cvut.fel.ear.library.model.Author;
import cz.cvut.fel.ear.library.model.Genre;
import cz.cvut.fel.ear.library.model.Title;
import cz.cvut.fel.ear.library.service.AuthorService;
import cz.cvut.fel.ear.library.service.GenreService;
import cz.cvut.fel.ear.library.service.TitleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("REST TitleController Test")
public class TitleControllerTest extends BaseControllerTestRunner {
    @Mock
    private TitleService service;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private TitleController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(controller);
    }

    @DisplayName("Get Titles Should Retrieve All Titles")
    @Test
    public void getTitleRetriveAllTitlees() throws Exception {
        List<Title> expected = IntStream.range(0, 10)
                .mapToObj(i -> {
                    Title temp = Generator.generateTitle();
                    temp.setAuthor(Generator.generateAuthor());
                    return temp;
                }).collect(Collectors.toList());
        Mockito.when(service.findAll()).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/api/titles")).andExpect(status().isOk()).andReturn();
        final List<Title> result = readValue(mvcResult, new TypeReference<List<Title>>() {
        });

        Assertions.assertEquals(expected.size(), result.size());
    }


    @DisplayName("Update Title Should Update Specific Title")
    @Test
    public void updateTitleShouldUpdateSpecificTitle() throws Exception {
        final Title toUpdate = Generator.generateTitle();
        final Title title = Generator.generateTitle();
        title.setId(toUpdate.getId());
        Mockito.when(service.find(Mockito.anyInt())).thenReturn(title);

        final MvcResult mvcResult = mockMvc.perform(post("/api/titles/{id}", title.getId())
                .content(toJson(toUpdate))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn();

        final ArgumentCaptor<Title> captor = ArgumentCaptor.forClass(Title.class);

        verifyLocationEquals("/api/titles/" + toUpdate.getId(), mvcResult);
        Mockito.verify(service).update(captor.capture());
    }






}
