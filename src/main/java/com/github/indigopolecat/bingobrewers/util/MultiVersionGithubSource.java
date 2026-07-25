package com.github.indigopolecat.bingobrewers.util;

import com.google.gson.JsonPrimitive;
import moe.nea.libautoupdate.GithubReleaseUpdateData;
import moe.nea.libautoupdate.GithubReleaseUpdateSource;
import moe.nea.libautoupdate.UpdateData;

/**
 * libautoupdate's own {@link GithubReleaseUpdateSource#findAsset} just picks the first
 * {@code .jar} asset on the release ({@code .findFirst()}, no filtering) - fine for a single-jar
 * release, but we publish one release with a jar per Minecraft version (bingobrewers-{version}+
 * {minecraft}.jar). This overrides asset selection to only match the jar built for the
 * Minecraft version this copy of the mod was compiled against.
 */
public class MultiVersionGithubSource extends GithubReleaseUpdateSource {
    private final String jarSuffix;

    public MultiVersionGithubSource(String owner, String repository, String minecraftVersion) {
        super(owner, repository);
        this.jarSuffix = "+" + minecraftVersion + ".jar";
    }

    @Override
    protected UpdateData findAsset(GithubRelease release) {
        if (release.getAssets() == null) return null;

        return release.getAssets().stream()
            .filter(it -> it.getName() != null && it.getName().endsWith(jarSuffix) && it.getBrowserDownloadUrl() != null)
            .findFirst()
            .<UpdateData>map(it -> new GithubReleaseUpdateData(
                release.getName() == null ? release.getTagName() : release.getName(),
                new JsonPrimitive(release.getTagName()),
                null,
                it.getBrowserDownloadUrl(),
                release.getBody(),
                release.getTargetCommitish(),
                release.getCreated_at(),
                release.getPublishedAt(),
                release.getHtmlUrl()
            ))
            .orElse(null);
    }
}
