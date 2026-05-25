package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ColourResponse {
    Integer page,
            per_page,
            total,
            total_pages;
    List<Colour> data;
    Support support;
}
