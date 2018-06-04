
package com.git.adaptor.gitresponse.pullrequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "url",
    "id",
    "html_url",
    "diff_url",
    "patch_url",
    "issue_url",
    "number",
    "state",
    "locked",
    "title",
    "user",
    "body",
    "created_at",
    "updated_at",
    "closed_at",
    "merged_at",
    "merge_commit_sha",
    "assignee",
    "assignees",
    "requested_reviewers",
    "requested_teams",
    "labels",
    "milestone",
    "commits_url",
    "review_comments_url",
    "review_comment_url",
    "comments_url",
    "statuses_url",
    "head",
    "base",
    "_links",
    "author_association"
})
public class OpenPR {

    @JsonProperty("url")
    public String url;
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("html_url")
    public String htmlUrl;
    @JsonProperty("diff_url")
    public String diffUrl;
    @JsonProperty("patch_url")
    public String patchUrl;
    @JsonProperty("issue_url")
    public String issueUrl;
    @JsonProperty("number")
    public Integer number;
    @JsonProperty("state")
    public String state;
    @JsonProperty("locked")
    public Boolean locked;
    @JsonProperty("title")
    public String title;
    @JsonProperty("user")
    public User user;
    @JsonProperty("body")
    public String body;
    @JsonProperty("created_at")
    public String createdAt;
    @JsonProperty("updated_at")
    public String updatedAt;
    @JsonProperty("closed_at")
    public Object closedAt;
    @JsonProperty("merged_at")
    public Object mergedAt;
    @JsonProperty("merge_commit_sha")
    public String mergeCommitSha;
    @JsonProperty("assignee")
    public Object assignee;
    @JsonProperty("assignees")
    public List<Object> assignees = null;
    @JsonProperty("requested_reviewers")
    public List<Object> requestedReviewers = null;
    @JsonProperty("requested_teams")
    public List<Object> requestedTeams = null;
    @JsonProperty("labels")
    public List<Object> labels = null;
    @JsonProperty("milestone")
    public Object milestone;
    @JsonProperty("commits_url")
    public String commitsUrl;
    @JsonProperty("review_comments_url")
    public String reviewCommentsUrl;
    @JsonProperty("review_comment_url")
    public String reviewCommentUrl;
    @JsonProperty("comments_url")
    public String commentsUrl;
    @JsonProperty("statuses_url")
    public String statusesUrl;
    @JsonProperty("head")
    public Head head;
    @JsonProperty("base")
    public Base base;
    @JsonProperty("_links")
    public Links links;
    @JsonProperty("author_association")
    public String authorAssociation;

}
