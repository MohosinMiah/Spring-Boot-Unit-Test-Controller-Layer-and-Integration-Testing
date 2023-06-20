# Spring-Boot-Unit-Test-Controller-Layer
Spring Boot - Unit Test Controller Layer

@WebMvcTest is a Spring Boot test annotation used to test the web layer of an application. It focuses on testing the controllers by simulating HTTP requests and verifying the responses.

Example :

```
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private EmployeeController EmployeeController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(EmployeeController).build();
    }

    @Test
    public void testGetAllBooks() throws Exception {
        Book book1 = new Book("1", "Book 1", "Author 1");
        Book book2 = new Book("2", "Book 2", "Author 2");
        List<Book> books = Arrays.asList(book1, book2);

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Book 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value("Author 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Book 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].author").value("Author 2"));
    }
}

```