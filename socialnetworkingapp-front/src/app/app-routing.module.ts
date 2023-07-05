import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./home";
import {LoginComponent} from "./login";
import {RegisterComponent} from "./register";
import {WelcomeComponent} from "./welcome/welcome.component";
import {SettingsComponent} from "./settings/settings.component";
import {AccountComponent} from "./account/account.component";
import {UserManagementComponent} from "./user-management/user-management.component";
import {NetworkComponent} from "./network/network.component";
import { AuthGuardService } from './authentication';
import {VisitAccountComponent} from "./visit-account/visit-account.component";
import { NotificationsComponent } from './notifications/notifications.component';
import {ReportedPostComponent} from "./reported-post/reported-post.component";

const routes: Routes = [
  { path: '',             component: WelcomeComponent },
  { path: 'login',        component: LoginComponent },
  { path: 'register',     component: RegisterComponent },
  { path: 'home',         component: HomeComponent ,        canActivate: [AuthGuardService]},
  { path: 'settings',     component: SettingsComponent,     canActivate: [AuthGuardService] },
  { path: 'account',      component: AccountComponent ,     canActivate: [AuthGuardService]},
  { path: 'visit/:email', component: VisitAccountComponent ,canActivate: [AuthGuardService]},
  { path: 'user-management',        component: UserManagementComponent,        canActivate: [AuthGuardService] },
  { path: 'reported-posts',        component: ReportedPostComponent,        canActivate: [AuthGuardService] },
  { path: 'network',      component: NetworkComponent ,     canActivate: [AuthGuardService]},
  { path: 'notifications',component: NotificationsComponent,canActivate: [AuthGuardService]},

  // otherwise, redirect to home
  { path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
