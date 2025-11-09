# README

## Introduction

This Project is designed to constitute a comprehensive framework for player community in Minecraft servers. It is built on
the IMYVMWorldGeo as an extension, which provides mechanics centered around **Region**, performing a Minecraft geography element.
We intend to offer an administration simulation system granting players the ability to self-govern their in-game regions
in the form of a player community.

## Features

### Community

A community is a player organization linked to a valid and exclusive region in the IMYVMWorldGeo.

#### Creation

A community can be initialized spontaneously by most of the players,
as long as a valid region is delineated by the same person, 
and the subsequent criteria are satisfied by the players creating the community:
- they are not yet members of any other community with the same type(elaborated below);
- they have enough in-game currency to pay for the community creation fee with the specified type;

By executing the creation through command or box-interface, the criteria are verified, and if all are satisfied,
the community is created with a pending status, awaiting certification.
Once being certified, the community is a sovereign entity that can direct its internal affairs,
with also foreign relations to be established with other communities online.

#### Types

Two types of communities have been defined by now, mainly differing in their population.
Of which the manor community is designed for small-scale player groups,
specifically catering to those who aspire to the features provided yet only for their own basement, 
which remain independent of large-scale settlements.
Nevertheless, further development of them must be within the framework of the type,
as they are founded with less expense and fewer member requirements.
In contrast, a realm community is intended for large-scale player organizations,
and it is expected to be the mainstream form of player community in the server.

To clarify, the creation of manor amounts to 15000 in the creator's in-game currency,
and it only requires 1 member, the creator themselves, 
with a maximum of 5 members allowed;
whereas a realm requires a creation fee of 30000 in-game currency,
and with at least 4 members at the time of creation.

#### Governance

A community of manor type is governed by its owner,
with two additional admins permitted.

A realm community allows for a more Byzantine governance architecture,
in which up to 3 admins can be designated at the level of the community.
For each scope of the community, a governor can be appointed to oversee its administration.
Meanwhile, a council can be established within the community,
comprising multiple councilors whose proposals to be a councilor 
are approved by processes defined by the community itself.
Two processes ratifying a proposal are currently supported,
namely voting by all members and decision by the community owner.
Whether the council is enabled or not is decided by the community owner.

In council, a vote comprises content, a duration and a motion,
which is proposed by a councilor or a councilor applicant.
It is passed only if more than half of total members voted,
and more than two thirds of votes are in favor within the duration.
Except for accepting new members of a council, 
votes can also be held for other important matters of the community,
encompassing amending community regulations, 
modifying regional settings,
regulating membership,
designating or dismissing officials,
and so forth.
The mechanics of voting grants members of a realm community the ability 
to participate in the governance of their community, and operate themselves even without an active owner.

## Acknowledgements

Were it not for the support of IMYVM fellows and players, this project would not have been possible.