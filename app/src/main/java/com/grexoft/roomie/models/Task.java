package com.grexoft.roomie.models;

import java.util.List;

/**
 * Created by sukrit on 12/10/17.
 */

public class Task {
    private final String name;
    private final String description;
    private final long startingTime;
    private final long repeatInterval;
    private final int numberOfTimes;
    private final List<User> participants;
    private final String taskStatus;
    private final long id;
    private final long notificationTime;

    private Task(TaskBuilder taskBuilder) {
        this.name = taskBuilder.name;
        this.description = taskBuilder.description;
        this.startingTime = taskBuilder.startingTime;
        this.repeatInterval = taskBuilder.repeatInterval;
        this.numberOfTimes = taskBuilder.numberOfTimes;
        this.participants = taskBuilder.participants;
        this.taskStatus = taskBuilder.taskStatus;
        this.id = taskBuilder.id;
        this.notificationTime = taskBuilder.notificationTime;

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getStartingTime() {
        return startingTime;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public int getNumberOfTimes() {
        return numberOfTimes;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public long getId() {
        return id;
    }

    public long getNotificationTime() {
        return notificationTime;
    }

    public static class TaskBuilder {
        private final String name;
        private String description;
        private long startingTime;
        private long repeatInterval;
        private int numberOfTimes;
        private List<User> participants;
        private String taskStatus;
        private long id;
        private long notificationTime;

        public TaskBuilder(String name) {
            this.name = name;
        }

        public TaskBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TaskBuilder startingTime(long startingTime) {
            this.startingTime = startingTime;
            return this;
        }

        public TaskBuilder repeatingInterval(long repeatInterval) {
            this.repeatInterval = repeatInterval;
            return this;
        }

        public TaskBuilder numberOfRepeatingTime(int numberOfTimes) {
            this.numberOfTimes = numberOfTimes;
            return this;

        }

        public TaskBuilder participants(List<User> users) {
            this.participants = users;
            return this;
        }

        public TaskBuilder taskStatus(String taskStatus) {
            this.taskStatus = taskStatus;
            return this;
        }

        public TaskBuilder id(long id) {
            this.id = id;
            return this;
        }

        public TaskBuilder notifyAtTime(long time) {
            this.notificationTime = time;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
