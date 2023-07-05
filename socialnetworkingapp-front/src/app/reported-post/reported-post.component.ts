import { Component, OnInit } from '@angular/core';
import {Account} from "../account/account";
import {HttpErrorResponse} from "@angular/common/http";
import {AccountService} from "../account/account.service";
import {ExportService} from "../export/export.service";
import {UploadFileService} from "../upload-files/upload-files.service";
import {BioService} from "../bio/bio.service";
import {Router} from "@angular/router";
import { AuthenticationService } from '../authentication';
import {Post} from "../post/post";
import {Comment} from "../comment/comment";
import {Like} from "../like/like";
import {LikeService} from "../like/like.service";
import {CommentService} from "../comment/comment.service";
import {PostViewService} from "../post-view/post-view.service";
import {PostService} from "../post/post.service";

@Component({
  selector: 'app-reported-post',
  templateUrl: './reported-post.component.html',
  styleUrls: ['./reported-post.component.css']
})
export class ReportedPostComponent implements OnInit {

  // public account: Account;
  public posts: Post[] = [];
  /* postId => [num of likes, If i have liked this post, show the like ID, else -1] */
  public likedPosts: Map<number, [number, number]> = new Map<number, [number, number]>();
  /* postId => [comments of post, every comment id I have made, else null] */
  public commentedPosts: Map<number, [Comment[], number[]]> = new Map<number, [Comment[], number[]]>();

  public postIdToDelete: number;
  public commentIdToDelete: number;
  public selectedPost: Post = undefined;

  public tempRefreshPostId: number;

  constructor(public router: Router,
              private authenticationService : AuthenticationService,
              private likeService: LikeService,
              private commentService: CommentService,
              private postViewService: PostViewService,
              private postService: PostService) {
  }

  ngOnInit() {

    //users cannot access reported-posts page
    if(this.authenticationService.isAdmin() === false){
      this.router.navigate(['/home']);
    }
    this.loadReportedPosts();
  }

  public loadPostLikes(pid: number) {
    this.likeService.getLikesOfPost(pid).subscribe(
      (likes: Like[]) => {
      }, (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public loadPostComments(pid: number) {
    this.commentService.getCommentsOfPost(pid).subscribe(
      (comments: any) => {
      }, (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  private loadReportedPosts() {
    this.postService.getReportedPosts().subscribe(
      (posts: Post[]) => {
        this.posts = posts;
        for(let post of this.posts) {
          this.loadPostLikes(post.id);
          this.loadPostComments(post.id);
        }
      }, (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onClickModal(data: any, mode: string): void {
    const container = document.getElementById('main-container');

    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');

    if(mode === 'deletePost') {
      this.postIdToDelete = data;
      button.setAttribute('data-target', '#deletePost');
    }
    if(mode === 'deleteComment') {
      this.commentIdToDelete = data[0];
      this.tempRefreshPostId = data[1];
      button.setAttribute('data-target', '#deleteComment');
    }
    if(mode === 'viewPost') {
      this.selectedPost = data;
      button.setAttribute('data-target', '#viewPost');
    }
    if(container != null) {
      container.appendChild(button);
      button.click();
    }
  }

  public onDeletePost(postIdToDelete: number) {
    this.postService.deletePost(postIdToDelete).subscribe(
      (event: any) => {
        this.loadReportedPosts();
      }, (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onDeleteComment(commentIdToDelete: number) {
    this.commentService.deleteComment(commentIdToDelete).subscribe(
      (event: any) => {
        this.loadPostComments(this.tempRefreshPostId);
      }, (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

}
