package com.github.codelomer.holyworldrtp.model;

import lombok.NonNull;

import java.util.List;
import java.util.UUID;

public record CustomRtpParams(@NonNull String name, int centerX, int centerZ, @NonNull RtpParams rtpParams, @NonNull List<RtpGroup> cooldownRtpGroups, @NonNull UUID worldUUID) {

}
