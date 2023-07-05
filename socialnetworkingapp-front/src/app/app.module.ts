import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {AccountService} from "./account/account.service";
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HomeComponent } from './home';
import { LoginComponent } from './login';
import { RegisterComponent } from './register';
import { AccountComponent } from './account/account.component';
import { BioComponent } from './bio/bio.component';
import { CommentComponent } from './comment/comment.component';
import { LikeComponent } from './like/like.component';
import { PostComponent } from './post/post.component';
import { SettingsComponent } from './settings/settings.component';
import { WelcomeComponent } from './welcome/welcome.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { UserManagementComponent } from './user-management/user-management.component';
import { NetworkComponent } from './network/network.component';
import { UploadFilesComponent } from './upload-files/upload-files.component';
import { NavBarAuthenticatedComponent } from './nav-bar-authenticated/nav-bar-authenticated.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { TagsComponent } from './tags/tags.component';
import { EducationComponent } from './education/education.component';
import { VisitAccountComponent } from './visit-account/visit-account.component';
import { ConnectionRequestComponent } from './connection-request/connection-request.component';
import { TokenInterceptorService } from './authentication';
import {NavBarAdminComponent} from "./nav-bar-admin/nav-bar-admin.component";
import { PostViewComponent } from './post-view/post-view.component';
import { NotificationsComponent } from './notifications/notifications.component';
import {ReportedPostComponent} from "./reported-post/reported-post.component";
@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    AccountComponent,
    BioComponent,
    CommentComponent,
    LikeComponent,
    PostComponent,
    SettingsComponent,
    WelcomeComponent,
    UserManagementComponent,
    ReportedPostComponent,
    NetworkComponent,
    UploadFilesComponent,
    NavBarAuthenticatedComponent,
    NavBarComponent,
    NavBarAdminComponent,
    TagsComponent,
    EducationComponent,
    VisitAccountComponent,
    ConnectionRequestComponent,
    PostViewComponent,
    NotificationsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgxWebstorageModule.forRoot()
  ],
  providers: [AccountService, {provide : HTTP_INTERCEPTORS , useClass : TokenInterceptorService , multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule { }
