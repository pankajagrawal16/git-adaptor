
package com.git.adaptor.gitresponse.pullrequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "self",
    "html",
    "issue",
    "comments",
    "review_comments",
    "review_comment",
    "commits",
    "statuses"
})
public class Links {

    @JsonProperty("self")
    public Self self;
    @JsonProperty("html")
    public Html html;
    @JsonProperty("issue")
    public Issue issue;
    @JsonProperty("comments")
    public Comments comments;
    @JsonProperty("review_comments")
    public ReviewComments reviewComments;
    @JsonProperty("review_comment")
    public ReviewComment reviewComment;
    @JsonProperty("commits")
    public Commits commits;
    @JsonProperty("statuses")
    public Statuses statuses;

}
