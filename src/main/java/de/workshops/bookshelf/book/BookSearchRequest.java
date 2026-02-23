package de.workshops.bookshelf.book;

import jakarta.validation.constraints.Size;

public record BookSearchRequest(@Size(min=5, max = 14) String isbn, String author) {
}