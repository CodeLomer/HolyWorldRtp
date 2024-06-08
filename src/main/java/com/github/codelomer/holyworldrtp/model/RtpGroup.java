package com.github.codelomer.holyworldrtp.model;


import lombok.NonNull;

import java.util.Objects;

public record RtpGroup(@NonNull String groupName, long value) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RtpGroup rtpGroup = (RtpGroup) o;
        return Objects.equals(groupName, rtpGroup.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName);
    }
}
