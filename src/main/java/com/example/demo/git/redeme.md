解决IDEA 每次使用 git pull/push 命令都需要输入账号密码的问题
git config --global credential.helper store

##列出标签
git tag
git tag -l "v1.8.5*"

## 附注标签
git tag -a v1.4 -m "my version 1.4"


git show v1.4

##轻量标签
git tag v1.4-lw