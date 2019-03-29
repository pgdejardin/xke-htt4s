-- Authors
insert into author(identifier, firstName, lastName)
values ('f90c77dc-0ea7-49eb-b806-6657f7a74fbd', 'Akira', 'Toriyama');

insert into author(identifier, firstName, lastName)
values ('1102f961-677a-4948-b98d-802140f1e8ab', 'Mitsuru', 'Adachi');

insert into author(identifier, firstName, lastName)
values ('2365416c-b5be-49e4-b48d-a259247462cb', 'Haruki', 'Murakami');

-- Books
insert into book(isbn, title, description, author_id)
values ('41282d63-f0bb-42ad-bb10-c7680921cd64',
        '羊をめぐる冒険 (Hitsuji o meguru bōken)',
        'La Course au mouton sauvage',
        '2365416c-b5be-49e4-b48d-a259247462cb');

insert into book(isbn, title, description, author_id)
values ('acff6032-be91-4d14-8039-61885f262026',
        '海辺のカフカ (Umibe no Kafuka)',
        'Kafka sur le rivage',
        '2365416c-b5be-49e4-b48d-a259247462cb');

insert into book(isbn, title, description, author_id)
values ('965b7a55-dea8-4020-97a7-d96e17395be0',
        'いちきゅうはちよん (Ichi-kyū-hachi-yon)',
        '1Q84',
        '2365416c-b5be-49e4-b48d-a259247462cb');
---
insert into book(isbn, title, description, author_id)
values ('9355adc7-080c-4f41-86ee-09dd1c48ec4e',
        'Rough (ラフ)',
        'Rough',
        '1102f961-677a-4948-b98d-802140f1e8ab');

insert into book(isbn, title, description, author_id)
values ('69eec3e3-edd3-4baf-9e7e-960f4865de8b',
        'Touch (タッチ)',
        'Touch',
        '1102f961-677a-4948-b98d-802140f1e8ab');

insert into book(isbn, title, description, author_id)
values ('51329ad7-7491-4dad-83aa-6a4e5329deb9',
        'Cross Game (クロスゲーム)',
        'Cross Game',
        '1102f961-677a-4948-b98d-802140f1e8ab');
---
insert into book(isbn, title, description, author_id)
values ('eae7865e-cdce-4f95-bb8b-0939a51d4210',
        'Dragon Ball',
        'Dragon Ball',
        'f90c77dc-0ea7-49eb-b806-6657f7a74fbd');

insert into book(isbn, title, description, author_id)
values ('c8917697-978f-4e9a-b33c-d1fd6b977a96',
        'Dr Slump',
        'Dr Slump',
        'f90c77dc-0ea7-49eb-b806-6657f7a74fbd');

insert into book(isbn, title, description, author_id)
values ('cc8be3e3-6f95-4926-822a-44fe34e5855a',
        'ネコマジン (Neko Majin)',
        'Neko Majin',
        'f90c77dc-0ea7-49eb-b806-6657f7a74fbd');
