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

A community can be initialized spontaneously by any player trying to inaugurate one,
as long as a valid region is delineated by the same person, 
and the subsequent criteria are satisfied:
- they are not yet members of any other community with the same type(elaborated below);
- they have enough in-game currency to pay for the community creation fee with the specified type;

Criteria are verified by executing the creation through command or box-interface, 
and then the community is created with a pending status, awaiting certification.
Once certified, the community is a sovereign entity that can direct its internal affairs,
as well as establish foreign relations with other communities online.

#### Types

Two types of communities have been defined so far, mainly differing in their population.
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
in which up to 3 admins can be designated at the community level.
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

#### Administration

From the administration standpoint, a community is endowed with various capabilities,
ranging from managing its members to regulating its region.

By now, a community can be renamed via modifications operated by owners or admins,
provided that they are unique around the server world and are not blank.
Later on, more community identifiers will be supported for alteration.
And permissions to modify them may be granted or revoked to admins or councilors as well,
depending on the preferences of the owner.

Members are players who joined the community member list as the owners, admins or regular members.
To acquire membership of a community, 
players must have not joined any other community with the identical type of the target community,
and the way they join still depends on the community citizenship policies defined by the community itself.
Communities have a few options for citizenship policies,
including open enrollment, invitation only, application with approval by owner/admins/council.
Members may be expelled from the community by owner/admins/council,
yet they can also voluntarily withdraw from it at any time,
unless they are owners.

The region of a community is designated at the time when the community launches, and is deemed as its territory.
A region is composed of multiple scopes that can be configured with different settings.
Intertwined with its region,
a community is entitled to expand or shrink its territory by adjusting the scopes of its region or adding/removing scopes,
to modify the settings of scopes,
or even to sell/buy scopes with other communities(if both parties agree and the trade will not result in regions without scopes),
but not to delete or unbind its region.

By definition, the assets and advancements of a community should be introduced as a part of this section as well.
Owing to complexity, however, their characteristics will be detailed independently in their own sections below.

#### Advancement

To develop a sense of community and diversity,
an advancement system is implemented guiding communities to achieve various milestones,
along with indexes helping to construct advancement items in detail.

#### Asset


#### Announcement



## Acknowledgements

Were it not for the support of IMYVM fellows and players, this project would not have been possible.