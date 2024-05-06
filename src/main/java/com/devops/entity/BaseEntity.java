package com.devops.entity;



import org.springframework.data.annotation.Id;

import java.util.UUID;

public abstract class BaseEntity {
    @Id
    protected UUID id;
}
