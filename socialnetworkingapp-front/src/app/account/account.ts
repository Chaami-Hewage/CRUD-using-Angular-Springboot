import {Bio} from "../bio/bio";
import {Tag} from "../tags/Tag";
import {Education} from "../education/education";
import {NetworkEntity} from "./network-entity";

export enum AppUserRole {
  ADMIN,
  PREMIUM_USER,
  REGULAR_USER,
}

export class Account {
  id: number;
  appUserRole: AppUserRole;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  tagId: number;
  tagName: string;
  imageUrl: string;
  dateCreated: Date;
  bio: Bio;
  network: NetworkEntity[];
  education: Array<Education>;

  constructor(acc: Account) {
    this.id = acc.id;
    this.appUserRole = acc.appUserRole;
    this.firstName = acc.firstName;
    this.lastName = acc.lastName;
    this.email = acc.email;
    this.phone = acc.phone;
    this.tagId = acc.tagId;
    this.tagName = acc.tagName;
    this.imageUrl = acc.imageUrl;
    this.dateCreated = acc.dateCreated;
    this.bio = acc.bio;
    this.network = acc.network;
    this.education = acc.education;
  }
}
