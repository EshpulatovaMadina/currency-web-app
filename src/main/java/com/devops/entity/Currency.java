package com.devops.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table("currency")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency {
    @Id
    private Long id;
    private Long bankId;
    @JsonProperty("Code")
    private Integer code;
    @JsonProperty("Ccy")
    private String ccy;
    @JsonProperty("CcyNm_UZ")
    private String ccyNmUz;
    @JsonProperty("Rate")
    private Double rate;
    @JsonProperty("Date")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate date;


    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", ccy='" + ccy + '\'' +
                ", ccyNmUz='" + ccyNmUz + '\'' +
                ", rate=" + rate +
                ", date=" + date +
                '}';
    }
}
