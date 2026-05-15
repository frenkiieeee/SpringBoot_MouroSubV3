CREATE SCHEMA IF NOT EXISTS mourosub;

CREATE TABLE IF NOT EXISTS public.admin_emails (
  email TEXT PRIMARY KEY
);

INSERT INTO public.admin_emails (email)
VALUES ('info@mourosub.com')
ON CONFLICT (email) DO NOTHING;
