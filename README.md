**Introduction**

The _json-rpc_ component is provides an implementation of the JSON-RPC specification 2.0.



**Prerequisites**

- Java 8
- Maven 3.3.x
- If you are using SSH to connect to GitHub (ie your origin URL looks like git@github.com:fclimited/json-rpc.git) 
you will need the following in ~/.ssh/config:
```
Host github.com
 Hostname ssh.github.com
 Port 443
 PreferredAuthentications publickey
 IdentitiesOnly yes
 IdentityFile ~/.ssh/id_rsa_github
 ProxyCommand nc -X CONNECT -x bne-app-proxy.au.fcl.internal:3128 %h %p
```

**Building**

```
git clone https://github.com/fclimited/json-rpc.git
    or
git clone git@github.com:fclimited/json-rpc.git
git co develop
mvn clean install
```


**Miscellaneous**

Clearly the revision control system for this project is git.
We use the GitFlow workflow for our basic branching strategy.
A <a href="https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow/">
great tutorial on Git workflows</a>, including GitFlow, can be found on Atlassian's tutorial site
and, for a background on the pros and cons of GitFlow, Vincent Driessen's blog post that introduced it can be found  
<a href="http://nvie.com/posts/a-successful-git-branching-model/">here</a>.

We find the following git <a href="https://git-scm.com/book/en/v2/Git-Basics-Git-Aliases">aliases</a> useful - you might 
consider adding them to your ~/.gitconfig file:
```
[color]
    ui = auto
[alias]
    st = status
    br = branch -av
    co = checkout
    ci = commit
    ca = commit -a
    lg = log --date=local
    lgn = log --date=local --name-only
    amend = commit --amend
    unstage = reset HEAD
    glog = log --graph --pretty=format:\"%Cred%h%Creset — %s %Cblue%an%Creset %Cgreen(%cr)%Creset\"
```
