-- Run on startup by Spring (init.mode=always)

create table if not exists participants (
  id uuid primary key,
  name text not null,
  phone text,
  "isPresent" boolean default true,
  created_at timestamptz default now()
);

create table if not exists draws (
  id uuid primary key,
  participant jsonb not null,
  "prizeType" text not null,
  "drawnAt" timestamptz default now()
);
