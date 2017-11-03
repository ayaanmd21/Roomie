package com.grexoft.roomie.models;


import java.io.Serializable;
import java.util.List;

public class Group implements Serializable{
    private final String groupName;
    private final String groupDescription;
    private final String groupPhotoUrl;
    private final int groupMembersCount;
    private final List<User> members;
    private final List<String> admins;
    private final long groupId;



    private Group(GroupBuilder groupBuilder) {
        this.groupName = groupBuilder.groupName;
        this.groupDescription = groupBuilder.groupDescription;
        this.groupPhotoUrl = groupBuilder.groupPhotoUrl;
        this.groupMembersCount = groupBuilder.groupMembersCount;
        this.members = groupBuilder.members;
        this.admins = groupBuilder.admins;
        this.groupId=groupBuilder.groupId;
    }

    public String getGroupName() {
        return groupName;
    }
    public long getGroupId() {
        return groupId;
    }
    public String getGroupDescription() {
        return groupDescription;
    }

    public String getGroupPhotoUrl() {
        return groupPhotoUrl;
    }

    public int getGroupMembersCount() {
        return groupMembersCount;
    }

    public List<User> getMembers() {
        return members;
    }

    public List<String> getAdmins() {
        return admins;
    }


    public static class GroupBuilder {
        private String groupName;
        private String groupDescription;
        private String groupPhotoUrl;
        private int groupMembersCount;
        private List<String> admins;
        private List<User> members;
        private long groupId;

        public GroupBuilder(String groupName) {
            this.groupName = groupName;
        }
        public  GroupBuilder id(long groupId){
            this.groupId=groupId;
            return this;
        }

        public GroupBuilder description(String groupDescription) {
            this.groupDescription = groupDescription;
            return this;
        }

        public GroupBuilder membersCount(int groupMembersCount) {
            this.groupMembersCount = groupMembersCount;
            return this;
        }

        public GroupBuilder admin(List<String> admin) {
            this.admins = admin;
            return this;
        }

        public GroupBuilder photoUrl(String groupPhotoUrl) {
            this.groupPhotoUrl = groupPhotoUrl;
            return this;
        }
        public GroupBuilder users(List<User> users){
            this.members=users;
            return this;
        }

        public Group build() {
            return new Group(this);
        }

    }




}
