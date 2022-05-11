package cz.cvut.fel.ear.library.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.cvut.fel.ear.library.environment.Generator;
import cz.cvut.fel.ear.library.model.Author;
import cz.cvut.fel.ear.library.model.Genre;
import cz.cvut.fel.ear.library.model.Title;
import cz.cvut.fel.ear.library.service.AuthorService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("REST TitleController Test")
public class AuthorControllerTest extends BaseControllerTestRunner {
    @Mock
    private AuthorService service;

    @InjectMocks
    private AuthorController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(controller);
    }

    @DisplayName("Get Authors Should Retrieve All Authors")
    @Test
    public void getAuthorsShouldRetrieveAllAuthors() throws Exception {
        final List<Author> expected = IntStream.range(0, 10)
                .mapToObj(i -> Generator.generateAuthor())
                .collect(Collectors.toList());
        Mockito.when(service.findAll()).thenReturn(expected);

        final MvcResult mvcResult = mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andReturn();
        final List<Author> result = readValue(mvcResult, new TypeReference<>() {
        });

        Assertions.assertEquals(expected.size(), result.size());
    }


    @DisplayName("Update Author Should Update Specific Author")
    @Test
    public void updateGenreShouldUpdateSpecificGenre() throws Exception {
        final Author toUpdate = Generator.generateAuthor();
        final Author author = new Author();
        author.setId(toUpdate.getId());
        Mockito.when(service.find(Mockito.anyInt())).thenReturn(author);

        final MvcResult mvcResult = mockMvc.perform(put("/api/authors/{id}", author.getId())
                .content(toJson(toUpdate))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn();

        final ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);

        verifyLocationEquals("/api/authors/" + toUpdate.getId(), mvcResult);
        Mockito.verify(service).update(captor.capture());
    }





}