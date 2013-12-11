namespace java andy

service Hello {
  string hi();
}

struct UserProfile {
  1: i32 uid,
  2: string name
}