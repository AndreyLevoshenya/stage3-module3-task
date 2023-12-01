package com.mjc.school.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;

import static com.mjc.school.helper.Constants.*;
import static com.mjc.school.helper.Operations.*;
import static com.mjc.school.helper.Utils.getLongFromKeyboard;
import static com.mjc.school.helper.Utils.getTagIdsFromKeyboard;

@Component
public class MenuHelper {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Function<Scanner, Command>> operations;
    private final PrintStream printStream = new PrintStream(System.out);

    public MenuHelper() {

        this.operations = new HashMap<>();

        operations.put(String.valueOf(GET_ALL_NEWS.getOperationNumber()), this::getAllNews);
        operations.put(String.valueOf(GET_NEWS_BY_ID.getOperationNumber()), this::getNewsById);
        operations.put(String.valueOf(CREATE_NEWS.getOperationNumber()), this::createNews);
        operations.put(String.valueOf(UPDATE_NEWS.getOperationNumber()), this::updateNews);
        operations.put(String.valueOf(REMOVE_NEWS_BY_ID.getOperationNumber()), this::removeNewsById);

        operations.put(String.valueOf(GET_ALL_AUTHORS.getOperationNumber()), this::getAllAuthors);
        operations.put(String.valueOf(GET_AUTHOR_BY_ID.getOperationNumber()), this::getAuthorById);
        operations.put(String.valueOf(CREATE_AUTHOR.getOperationNumber()), this::createAuthor);
        operations.put(String.valueOf(UPDATE_AUTHOR.getOperationNumber()), this::updateAuthor);
        operations.put(String.valueOf(REMOVE_AUTHOR_BY_ID.getOperationNumber()), this::removeAuthorById);

        operations.put(String.valueOf(GET_ALL_TAGS.getOperationNumber()), this::getAllTags);
        operations.put(String.valueOf(GET_TAG_BY_ID.getOperationNumber()), this::getTagById);
        operations.put(String.valueOf(CREATE_TAG.getOperationNumber()), this::createTag);
        operations.put(String.valueOf(UPDATE_TAG.getOperationNumber()), this::updateTag);
        operations.put(String.valueOf(REMOVE_TAG_BY_ID.getOperationNumber()), this::removeTagById);

        operations.put(String.valueOf(GET_AUTHOR_BY_NEWS_ID.getOperationNumber()), this::getAuthorByNewsId);
        operations.put(String.valueOf(GET_TAGS_BY_NEWS_ID.getOperationNumber()), this::getTagByNewsId);
        operations.put(String.valueOf(GET_NEWS_BY_PARAMS.getOperationNumber()), this::getNewsByParams);
    }

    public void printMenu() {
        printStream.println(ENTER_NUMBER_OF_OPERATION);
        for (Operations operation : Operations.values()) {
            printStream.println(operation.getOperationWithNumber());
        }
    }

    public Command getCommand(String key, Scanner scanner) {
        return operations.getOrDefault(key, this::getCommandNotFound).apply(scanner);
    }

    private Command getCommandNotFound(Scanner scanner) {
        return Command.COMMAND_NOT_FOUND;
    }

    private Command getAllNews(Scanner scanner) {
        printStream.println(GET_ALL_NEWS.getOperation());
        return Command.GET_ALL_NEWS_COMMAND;
    }

    private Command getNewsById(Scanner scanner) {
        printStream.println(GET_NEWS_BY_ID.getOperation());

        printStream.println(ENTER_NEWS_ID);
        Long id = getLongFromKeyboard(NEWS_ID);

        return new Command(GET_NEWS_BY_ID.getOperationNumber(), Map.of("id", String.valueOf(id)), null);
    }

    private Command createNews(Scanner scanner) {
        printStream.println(CREATE_NEWS.getOperation());
        printStream.println(ENTER_NEWS_TITLE);
        String title = scanner.nextLine();
        printStream.println(ENTER_NEWS_CONTENT);
        String content = scanner.nextLine();
        printStream.println(ENTER_AUTHOR_ID);
        Long authorId = getLongFromKeyboard(AUTHOR_ID);
        Set<Long> tagIds = getTagIdsFromKeyboard();

        try {
            Map<String, Object> body = new HashMap<>();
            body.put("title", title);
            body.put("content", content);
            body.put("authorId", String.valueOf(authorId));
            body.put("tagIds", tagIds);

            return new Command(CREATE_NEWS.getOperationNumber(), null, objectMapper.writeValueAsString(body));
        } catch (Exception e) {
            printStream.println(e.getMessage());
            return null;
        }
    }

    private Command updateNews(Scanner scanner) {
        printStream.println(UPDATE_NEWS.getOperation());

        printStream.println(ENTER_NEWS_ID);
        Long id = getLongFromKeyboard(NEWS_ID);
        printStream.println(ENTER_NEWS_TITLE);
        String title = scanner.nextLine();
        printStream.println(ENTER_NEWS_CONTENT);
        String content = scanner.nextLine();
        printStream.println(ENTER_AUTHOR_ID);
        Long authorId = getLongFromKeyboard(AUTHOR_ID);

        try {
            Map<String, String> body = Map.of(
                    "id", String.valueOf(id),
                    "title", title,
                    "content", content,
                    "authorId", String.valueOf(authorId));

            return new Command(UPDATE_NEWS.getOperationNumber(), null, objectMapper.writeValueAsString(body));
        } catch (Exception e) {
            printStream.println(e.getMessage());
            return null;
        }
    }

    private Command removeNewsById(Scanner scanner) {
        printStream.println(REMOVE_NEWS_BY_ID.getOperation());

        printStream.println(ENTER_NEWS_ID);
        Long id = getLongFromKeyboard(NEWS_ID);

        return new Command(REMOVE_NEWS_BY_ID.getOperationNumber(), Map.of("id", String.valueOf(id)), null);
    }

    private Command getAllAuthors(Scanner scanner) {
        printStream.println(GET_ALL_AUTHORS.getOperation());

        return Command.GET_ALL_AUTHORS_COMMAND;
    }

    private Command getAuthorById(Scanner scanner) {
        printStream.println(GET_AUTHOR_BY_ID.getOperation());

        printStream.println(ENTER_AUTHOR_ID);
        Long id = getLongFromKeyboard(AUTHOR_ID);

        return new Command(GET_AUTHOR_BY_ID.getOperationNumber(), Map.of("id", String.valueOf(id)), null);
    }

    private Command createAuthor(Scanner scanner) {
        printStream.println(CREATE_AUTHOR.getOperation());

        printStream.println(ENTER_AUTHOR_NAME);
        String name = scanner.nextLine();

        Map<String, String> body = Map.of("name", name);
        try {
            return new Command(CREATE_AUTHOR.getOperationNumber(), null, objectMapper.writeValueAsString(body));
        } catch (Exception e) {
            printStream.println(e.getMessage());
            return null;
        }
    }

    private Command updateAuthor(Scanner scanner) {
        printStream.println(UPDATE_AUTHOR.getOperation());

        printStream.println(ENTER_AUTHOR_ID);
        Long id = getLongFromKeyboard(AUTHOR_ID);
        printStream.println(ENTER_AUTHOR_NAME);
        String name = scanner.nextLine();

        Map<String, String> body = Map.of(
                "id", String.valueOf(id),
                "name", name);
        try {
            return new Command(UPDATE_AUTHOR.getOperationNumber(), null, objectMapper.writeValueAsString(body));
        } catch (Exception e) {
            printStream.println(e.getMessage());
            return null;
        }
    }

    private Command removeAuthorById(Scanner scanner) {
        printStream.println(REMOVE_AUTHOR_BY_ID.getOperation());

        printStream.println(ENTER_AUTHOR_ID);
        Long id = getLongFromKeyboard(AUTHOR_ID);

        return new Command(REMOVE_AUTHOR_BY_ID.getOperationNumber(), Map.of("id", String.valueOf(id)), null);
    }

    private Command getAllTags(Scanner scanner) {
        printStream.println(GET_ALL_TAGS.getOperation());

        return Command.GET_ALL_TAGS_COMMAND;
    }

    private Command getTagById(Scanner scanner) {
        printStream.println(GET_TAG_BY_ID.getOperation());

        printStream.println(ENTER_TAG_ID);
        Long id = getLongFromKeyboard(TAG_ID);

        return new Command(GET_TAG_BY_ID.getOperationNumber(), Map.of("id", String.valueOf(id)), null);
    }

    private Command createTag(Scanner scanner) {
        printStream.println(CREATE_TAG.getOperation());

        printStream.println(ENTER_TAG_NAME);
        String name = scanner.nextLine();

        Map<String, String> body = Map.of("name", name);
        try {
            return new Command(CREATE_TAG.getOperationNumber(), null, objectMapper.writeValueAsString(body));
        } catch (Exception e) {
            printStream.println(e.getMessage());
            return null;
        }
    }

    private Command updateTag(Scanner scanner) {
        printStream.println(UPDATE_TAG.getOperation());

        printStream.println(ENTER_TAG_ID);
        Long id = getLongFromKeyboard(TAG_ID);
        printStream.println(ENTER_TAG_NAME);
        String name = scanner.nextLine();

        Map<String, String> body = Map.of(
                "id", String.valueOf(id),
                "name", name);
        try {
            return new Command(UPDATE_TAG.getOperationNumber(), null, objectMapper.writeValueAsString(body));
        } catch (Exception e) {
            printStream.println(e.getMessage());
            return null;
        }
    }

    private Command removeTagById(Scanner scanner) {
        printStream.println(REMOVE_TAG_BY_ID.getOperation());

        printStream.println(ENTER_TAG_ID);
        Long id = getLongFromKeyboard(TAG_ID);

        return new Command(REMOVE_TAG_BY_ID.getOperationNumber(), Map.of("id", String.valueOf(id)), null);
    }

    private Command getAuthorByNewsId(Scanner scanner) {
        printStream.println(GET_AUTHOR_BY_NEWS_ID.getOperation());

        printStream.println(ENTER_NEWS_ID);
        Long id = getLongFromKeyboard(NEWS_ID);

        return new Command(GET_AUTHOR_BY_NEWS_ID.getOperationNumber(), Map.of("id", String.valueOf(id)), null);
    }

    private Command getTagByNewsId(Scanner scanner) {
        printStream.println(GET_TAGS_BY_NEWS_ID.getOperation());

        printStream.println(ENTER_NEWS_ID);
        Long id = getLongFromKeyboard(NEWS_ID);

        return new Command(GET_TAGS_BY_NEWS_ID.getOperationNumber(), Map.of("id", String.valueOf(id)), null);
    }

    private Command getNewsByParams(Scanner scanner) {
        printStream.println(GET_NEWS_BY_PARAMS.getOperation());

        printStream.println(ENTER_TAG_NAME);
        String tagName = scanner.nextLine();
        if (tagName.isEmpty()) {
            tagName = null;
        }
        printStream.println(ENTER_TAG_ID);
        Long tagId = getLongFromKeyboard(TAG_ID);
        printStream.println(ENTER_AUTHOR_NAME);
        String authorName = scanner.nextLine();
        if (authorName.isEmpty()) {
            authorName = null;
        }
        printStream.println(ENTER_NEWS_TITLE);
        String newsTitle = scanner.nextLine();
        if (newsTitle.isEmpty()) {
            newsTitle = null;
        }
        printStream.println(ENTER_NEWS_CONTENT);
        String newsContent = scanner.nextLine();
        if (newsContent.isEmpty()) {
            newsContent = null;
        }

        Map<String, String> body = Map.of(
                "tagName", tagName,
                "tagId", String.valueOf(tagId),
                "authorName", authorName,
                "newsTitle", newsTitle,
                "newsContent", newsContent);
        try {
            return new Command(GET_NEWS_BY_PARAMS.getOperationNumber(), null, objectMapper.writeValueAsString(body));
        } catch (Exception e) {
            printStream.println(e.getMessage());
            return null;
        }
    }
}
