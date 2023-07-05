import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Post} from "./post";

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private url = environment.apiBaseUrl + '/posts';
  constructor(private http: HttpClient) {  }

  public addPost(value: any): Observable<Post> {
    return this.http.post<Post>(`${this.url}/add`, value );
  }

  public getPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.url}/all`);
  }

  public getPostsByTag(tagId: number): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.url}/all/${tagId}`);
  }

  public getReportedPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.url}/all-reported`);
  }

  public updatePost(pid: number, payload: string): Observable<any> {
    return this.http.put<any>(`${this.url}/update/${pid}`, payload);
  }

  public deletePost(pid: number): Observable<any> {
    return this.http.delete<void>(`${this.url}/delete/${pid}`);
  }

  public reportPost(pid: number): Observable<any> {
    return this.http.get<void>(`${this.url}/report/${pid}`);
  }
}
