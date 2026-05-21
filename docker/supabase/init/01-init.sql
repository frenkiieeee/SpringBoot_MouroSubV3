do $$
begin

  -- Roles base Supabase
  if not exists (select 1 from pg_roles where rolname = 'anon') then
    create role anon noinherit;
  end if;

  if not exists (select 1 from pg_roles where rolname = 'authenticated') then
    create role authenticated noinherit;
  end if;

  if not exists (select 1 from pg_roles where rolname = 'service_role') then
    create role service_role noinherit;
  end if;

  -- Usuario admin requerido por Studio / Meta
  if not exists (select 1 from pg_roles where rolname = 'supabase_admin') then
    create role supabase_admin
    with
      login
      superuser
      createdb
      createrole
      password 'grupo3';
  end if;

end
$$;

create schema if not exists auth;
create schema if not exists storage;

grant usage on schema public to anon, authenticated, service_role;

grant anon to postgres;
grant authenticated to postgres;
grant service_role to postgres;