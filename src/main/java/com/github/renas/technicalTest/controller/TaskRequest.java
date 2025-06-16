package com.github.renas.technicalTest.controller;


import java.sql.Date;
import java.util.UUID;

public record TaskRequest(String name, String description, Date dueDate) {}
