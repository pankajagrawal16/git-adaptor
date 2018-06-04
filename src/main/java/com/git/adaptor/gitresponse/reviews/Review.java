
package com.git.adaptor.gitresponse.reviews;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "user",
    "body",
    "state",
    "html_url",
    "pull_request_url",
    "author_association",
    "_links",
    "submitted_at",
    "commit_id"
})
public class Review {

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("user")
    public User user;
    @JsonProperty("body")
    public String body;
    @JsonProperty("state")
    public String state;
    @JsonProperty("html_url")
    public String htmlUrl;
    @JsonProperty("pull_request_url")
    public String pullRequestUrl;
    @JsonProperty("author_association")
    public String authorAssociation;
    @JsonProperty("_links")
    public Links links;
    @JsonProperty("submitted_at")
    public String submittedAt;
    @JsonProperty("commit_id")
    public String commitId;

    public ZonedDateTime getSubmittedAtAsDate() {
        return ZonedDateTime.parse(submittedAt);
    }
}
