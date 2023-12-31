import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, FormArray } from "@angular/forms";
import { AccountService } from "../account/account.service";
import { HttpEventType, HttpResponse } from "@angular/common/http";
import { Account } from "../account/account";
import { Router } from '@angular/router';
import { UploadFileService } from "../upload-files/upload-files.service";
import { BioService } from "../bio/bio.service";
import { AuthenticationService } from '../authentication';
import { TagsService } from "../tags/tags.service";
import {Tag} from '../tags/Tag'

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;
  correctCredentials = true;
  selectedFiles: FileList;
  currentFile: File;
  progress = 0;
  message = '';
  tagsArray : Tag[];
  showFields = false;

    private account: Account;

    constructor(
        private accountService: AccountService,
        private authService: AuthenticationService,
        public router: Router,
        private uploadService: UploadFileService,
        private bioService: BioService,
        private fb: FormBuilder,
        private tagsService: TagsService,
        private authenticationService : AuthenticationService

    ) { }

    ngOnInit(): void {
        if (this.authService.isLoggedIn()) {
            this.router.navigate(['/home']);
        }

        this.registerForm = this.fb.group({
            email: new FormControl('', [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]),
            password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(16)]),
            confPassword: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(16)]),
            firstName: new FormControl('', [Validators.required, Validators.maxLength(20), Validators.pattern('[a-zA-Z ]*')]),
            lastName: new FormControl('', [Validators.required, Validators.maxLength(20), Validators.pattern('[a-zA-Z ]*')]),
            phone: new FormControl('', [Validators.required, Validators.pattern('[0-9]*'), Validators.maxLength(15)]),
            accountType: new FormControl('', Validators.required),
            tagId: new FormControl(null),
        },
            { validator: this.checkPasswords }
        );

        this.tagsService.getAllTags().subscribe(
        (response: Tag[]) => {
            this.tagsArray= response;
            }
        );

    }

    selectFile(event: any) {
        this.selectedFiles = event.target.files;
    }

    checkPasswords(group: FormGroup) {
        const password = group.controls.password.value;
        const confirm_password = group.controls.confPassword.value;
        return password === confirm_password ? null : { notSame: true };
    }

    upload() {
        this.progress = 0;
        this.currentFile = this.selectedFiles.item(0);
        this.uploadService.uploadUser(this.currentFile, this.authenticationService.getJWT()).subscribe(
            event => {
                if (event.type === HttpEventType.UploadProgress) {
                    this.progress = Math.round(100 * event.loaded / event.total);
                } else if (event instanceof HttpResponse) {
                    this.message = event.body.message;
                }
                this.onClickModal('uploadSuccessful');
            },
            err => {
                this.progress = 0;
                this.message = 'Could not upload the file!';
                this.currentFile = undefined;
            });

        this.selectedFiles = undefined;
    }

  public onRegister(registerForm: FormGroup): void {

    this.accountService.registerAccount(registerForm.value).subscribe(
      (response: Account) => {
        this.authService.logIn({ 'username': registerForm.get('email')?.value, 'password': registerForm.get('password')?.value }).subscribe(
          (newResponse: boolean) => {
            this.onClickModal('addPhoto');
          },
          (error) => {
            console.log(error);
          }
        )
      },
      (error: any) => {
        this.correctCredentials = false;
        this.registerForm.reset();
        console.log(error);
      }
    );

  }

    public onClickModal(mode: string): void {
        const container = document.getElementById('main-container');

        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');

        if (mode === 'addPhoto') {
            button.setAttribute('data-target', '#addPhoto');
        }
        if (mode === 'uploadSuccessful') {
          button.setAttribute('data-target', '#uploadSuccessful');
        }
        if (container != null) {
            container.appendChild(button);
            button.click();
        }

    }

  public goToHomePage() {
    window.location.reload();
    this.router.navigateByUrl('/home');
  }
}
