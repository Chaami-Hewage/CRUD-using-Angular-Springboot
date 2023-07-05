import { Component, OnInit } from '@angular/core';
import { Account } from "./account";
import { AccountService } from "./account.service";
import { HttpErrorResponse } from "@angular/common/http";
import { ActivatedRoute, Router } from "@angular/router";
import { Bio } from "../bio/bio";
import { AuthenticationService } from '../authentication';
import { Education } from "../education/education";
import { EducationService } from "../education/education.service";
import { FormBuilder, FormControl, Validators, FormGroup, FormArray } from '@angular/forms';
import { TagsService } from '../tags/tags.service';
import { Tag } from '../tags/Tag'

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {

  public account: Account;
  public selectedEdu: Education;
  public TagsArray: Tag[];

  public endingDateVisibility: boolean = false;

  aboutForm: FormGroup;
  bioForm: FormGroup;
  addEducationForm: FormGroup;
  editEducationForm: FormGroup;

  constructor(private accountService: AccountService,
              private route: ActivatedRoute,
              public authenticationService: AuthenticationService,
              private educationService: EducationService,
              private router: Router,
              private fb: FormBuilder,
              private tagsService: TagsService
  ) { }

  public fetchUserInfo() {
    let email = this.authenticationService.getCurrentUser();
    this.accountService.fetchUser(email).subscribe(
      (response: Account) => {
        this.account = new Account(response);
        this.account.education = this.account.education.reverse();
        if (this.account.bio === null) {
          this.account.bio = new Bio("No available bio.");
        }
      }
    );
  }

  ngOnInit(): void {
    this.fetchUserInfo();

    this.account?.appUserRole.toString();

    this.aboutForm = this.fb.group({
        email: new FormControl(this.authenticationService.getCurrentUser()),
        firstName: new FormControl('', [Validators.maxLength(20), Validators.pattern('[a-zA-Z ]*')]),
        lastName: new FormControl('', [Validators.maxLength(20), Validators.pattern('[a-zA-Z ]*')]),
        phone: new FormControl('', [Validators.pattern('[0-9]*'), Validators.maxLength(15)]),
      },
    );

    this.bioForm = this.fb.group({
        email: new FormControl(this.authenticationService.getCurrentUser()),
        description: new FormControl('')
      },
    );

    this.addEducationForm = this.fb.group({
        school: new FormControl(''),
        degree: new FormControl(''),
        field: new FormControl(''),
        startDate: new FormControl(''),
        endDate: new FormControl(''),
        grade: new FormControl(''),
        description: new FormControl(''),
      },
    );

    this.editEducationForm = this.fb.group({
        id: new FormControl(''),
        school: new FormControl(''),
        degree: new FormControl(''),
        field: new FormControl(''),
        startDate: new FormControl(''),
        endDate: new FormControl(''),
        grade: new FormControl(''),
        description: new FormControl(''),
      },
    );

    this.tagsService.getAllTags().subscribe(
      (response: Tag[]) => {
        this.TagsArray = response;
      }
    );
  }

  public onClickModal(data: any, mode: string): void {

    const container = document.getElementById('main-container');

    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');

    if (mode === 'about') {
      button.setAttribute('data-target', '#about');
    }
    if (mode === 'bio') {
      button.setAttribute('data-target', '#biom');
    }
    if (mode === 'education') {
      this.selectedEdu = data;
      button.setAttribute('data-target', '#educationm');
    }
    if (mode === 'add_education') {
      button.setAttribute('data-target', '#educationadd');
    }
    if (mode === 'deleteEducation') {
      this.selectedEdu = data;
      button.setAttribute('data-target', '#educationdelete');
    }
    if (container != null) {
      container.appendChild(button);
      button.click();
    }
  }

  public aboutSubmit(aboutForm: FormGroup) {

    this.accountService.aboutUpdateAccount(aboutForm.value).subscribe(
      (response: Account) => {
        this.fetchUserInfo();
      },
      (error: any) => {
        this.aboutForm.reset();
        console.log(error);
      }
    );


  }

  public bioSubmit(bioSubmit: FormGroup) {

    this.accountService.addBio(bioSubmit.value.description).subscribe(
      (response: Bio) => {
        this.fetchUserInfo();
      },
      (error: any) => {
        this.aboutForm.reset();
        console.log(error);
      }
    );
  }

  public addEducation(addEducationForm: FormGroup) {

    this.accountService.addEducation(addEducationForm.value).subscribe(
      (response: Account) => {
        addEducationForm.reset();
        window.location.reload();
      },
      (error: any) => {
        this.addEducationForm.reset();
        console.log(error);
      }
    );
  }

  public editEducation(editEducationForm: FormGroup, selectedEdu: Education) {

    editEducationForm.get('id')?.setValue(selectedEdu.id);
    editEducationForm.get('visible')?.setValue(selectedEdu.visible);
    this.accountService.editEducation(editEducationForm.value).subscribe(
      (response: Education) => {
        editEducationForm.reset();
        window.location.reload();
      },
      (error: any) => {
        this.aboutForm.reset();
        console.log(error);
      }
    );
  }

  public deleteEducation(selectedEdu: Education) {
    this.accountService.deleteEducation(selectedEdu.id).subscribe(
      (response: void) => {
        this.fetchUserInfo();
      },
      (err: HttpErrorResponse) => {
        console.log(err);
        // this.interestsForm.reset();
      }
    );
  }

  public changeEndingDateVisibility() {
    if(this.endingDateVisibility) {
      this.endingDateVisibility = false;
    } else {
      this.endingDateVisibility = true;
    }
  }
}
