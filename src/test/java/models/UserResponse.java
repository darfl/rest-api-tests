package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserResponse {
    Integer page,
            per_page,
            total,
            total_pages;
    List<User> data;
    Support support;
}
