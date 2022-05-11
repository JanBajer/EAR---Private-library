package cz.cvut.fel.ear.library.rest;


import com.fasterxml.jackson.core.type.TypeReference;
import cz.cvut.fel.ear.library.environment.Generator;
import cz.cvut.fel.ear.library.model.Genre;
import cz.cvut.fel.ear.library.service.GenreService;
import cz.cvut.fel.ear.library.service.TitleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@DisplayName("REST GenreController Test")
public class GenreControllerTest extends BaseControllerTestRunner {
    @Mock
    private GenreService service;

    @Mock
    private TitleService titleService;

    @InjectMocks
    private GenreController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(controller);
    }

    @DisplayName("Get Genres Should Retrieve All Genres")
    @Test
    public void getGenresRetriveAllGenres() throws Exception {
        final List<Genre> expected = IntStream.range(0, 10)
                .mapToObj(i -> Generator.generateGenre())
                .collect(Collectors.toList());
        when(service.findAll()).thenReturn(expected);

        final MvcResult mvcResult = mockMvc.perform(get("/api/genres")).andReturn();
        final List<Genre> result = readValue(mvcResult, new TypeReference<>() {});

        Mockito.verify(service).findAll();
        Assertions.assertEquals(expected.size(), result.size());
    }


}
